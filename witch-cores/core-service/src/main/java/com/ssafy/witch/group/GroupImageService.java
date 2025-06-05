package com.ssafy.witch.group;

import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.exception.file.UnsupportedFileFormatException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.file.FileOwnerCachePort;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.PresignedUrlPort;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.group.command.UpdateGroupImageCommand;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupImageService implements GroupImageUseCase {

  private static final String PROFILE_DIRECTORY = "group/";
  private static final List<String> ALLOWED_FILE_EXT = List.of("jpg", "jpeg", "png");

  private final PresignedUrlPort presignedUrlPort;
  private final FileOwnerCachePort fileOwnerCachePort;
  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;

  @Override
  public PresignedUrl generateGroupImagePresignedUrl(GeneratePresignedUrlCommand command) {
    String userId = command.getUserId();
    String fileName = command.getFileName();

    validateExt(fileName);

    PresignedUrl presignedUrl = presignedUrlPort.generatePresignedUrl(fileName, PROFILE_DIRECTORY);

    // caching ownership
    fileOwnerCachePort.save(presignedUrl.getObjectKey(), userId, Duration.ofMinutes(10));

    return presignedUrl;
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

  @Override
  public void updateGroupImageUrl(UpdateGroupImageCommand command) {
    String userId = command.getUserId();
    String groupId = command.getGroupId();
    String objectKey = command.getObjectKey();

    // 모임 존재 확인
    Group group = groupPort.findById(groupId).orElseThrow(GroupNotFoundException::new);

    //이미지 삭제하는 경우
    if (objectKey == null || objectKey.isBlank()) {
      // 저장
      group.changeGroupImage(presignedUrlPort.getAccessUrl(objectKey));
      groupPort.save(group);
      return;
    }

    String ownerId= fileOwnerCachePort.getOwnerId(objectKey);
    fileOwnerCachePort.delete(objectKey);

    // 권한 있는지 확인
    validateLeaderAuthorization(userId, groupId);

    //파일 소유자 확인
    if(!userId.equals(ownerId)) {
      throw new InvalidFileOwnerException();
    }

    group.changeGroupImage(presignedUrlPort.getAccessUrl(objectKey));
    groupPort.save(group);
  }


  private void validateLeaderAuthorization(String userId, String groupId) {
    if (!groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }
}
