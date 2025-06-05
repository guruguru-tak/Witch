package com.ssafy.witch.group;

import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.group.command.UpdateGroupImageCommand;

public interface GroupImageUseCase {

  PresignedUrl generateGroupImagePresignedUrl(GeneratePresignedUrlCommand command);

  void updateGroupImageUrl(UpdateGroupImageCommand command);
}
