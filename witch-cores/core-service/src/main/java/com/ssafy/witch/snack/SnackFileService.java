package com.ssafy.witch.snack;

import com.ssafy.witch.exception.file.UnsupportedFileFormatException;
import com.ssafy.witch.file.FileOwnerCachePort;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.PresignedUrlPort;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SnackFileService implements SnackFileUseCase {

  private static final String FILE_DIRECTORY = "snack/";
  private static final List<String> ALLOWED_FILE_EXT = List.of("jpg", "jpeg", "png", "mp3", "mpeg");

  private final PresignedUrlPort presignedUrlPort;
  private final FileOwnerCachePort fileOwnerCachePort;

  @Override
  public PresignedUrl generateSnackFilePresignedUrl(GeneratePresignedUrlCommand command) {
    String userId = command.getUserId();
    String fileName = command.getFileName();

    validateExt(fileName);

    PresignedUrl presignedUrl = presignedUrlPort.generatePresignedUrl(fileName, FILE_DIRECTORY);

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
}
