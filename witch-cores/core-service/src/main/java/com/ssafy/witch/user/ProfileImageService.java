package com.ssafy.witch.user;

import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.exception.file.UnsupportedFileFormatException;
import com.ssafy.witch.exception.user.UserNotFoundException;
import com.ssafy.witch.file.FileOwnerCachePort;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.PresignedUrlPort;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.user.command.UpdateProfileImageCommand;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class ProfileImageService implements ProfileImageUseCase {

  private static final String PROFILE_DIRECTORY = "profile/";
  private static final List<String> ALLOWED_FILE_EXT = List.of("jpg", "jpeg", "png");

  private final PresignedUrlPort presignedUrlPort;
  private final FileOwnerCachePort fileOwnerCachePort;
  private final UserPort userPort;

  @Override
  public PresignedUrl generateProfileImagePresignedUrl(GeneratePresignedUrlCommand command) {
    String userId = command.getUserId();
    String fileName = command.getFileName();

    validateExt(fileName);

    PresignedUrl presignedUrl = presignedUrlPort.generatePresignedUrl(fileName, PROFILE_DIRECTORY);

    // caching ownership
    fileOwnerCachePort.save(presignedUrl.getObjectKey(), userId, Duration.ofMinutes(10));

    return presignedUrl;
  }

  @Override
  public void updateProfileImageUrl(UpdateProfileImageCommand command) {
    String userId = command.getUserId();
    String objectKey = command.getObjectKey();

    User user = userPort.findById(userId).orElseThrow(UserNotFoundException::new);
    if (objectKey == null || objectKey.isBlank()) {
      user.changeProfileImage(presignedUrlPort.getAccessUrl(objectKey));
      userPort.save(user);
      return;
    }

    String ownerId = fileOwnerCachePort.getOwnerId(objectKey);
    fileOwnerCachePort.delete(objectKey);

    if (!userId.equals(ownerId)) {
      throw new InvalidFileOwnerException();
    }

    user.changeProfileImage(presignedUrlPort.getAccessUrl(objectKey));
    userPort.save(user);
  }

  private void validateExt(String fileName) {
    String ext = extractExt(fileName);
    if (!ALLOWED_FILE_EXT.contains(ext)) {
      throw new UnsupportedFileFormatException();
    }
  }

  private String extractExt(String fileName) {
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }


}
