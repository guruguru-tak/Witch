package com.ssafy.witch.group;

import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.file.FileOwnerCachePort;
import com.ssafy.witch.file.PresignedUrlPort;
import com.ssafy.witch.group.command.GroupCreateCommand;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateGroupService implements CreateGroupUseCase {

  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;

  private final GroupValidateService groupValidateService;

  private final FileOwnerCachePort fileOwnerCachePort;
  private final PresignedUrlPort presignedUrlPort;

  @Transactional
  @Override
  public void createGroup(GroupCreateCommand command) {
    String userId = command.getUserId();
    String name = command.getName();
    String groupImageObjectKey = command.getGroupImageObjectKey();

    groupValidateService.checkGroupNameDuplication(name);

    if (Objects.nonNull(groupImageObjectKey)) {
      validateOwnership(groupImageObjectKey, userId);
    }

    String groupImageUrl = presignedUrlPort.getAccessUrl(groupImageObjectKey);

    Group group = Group.createNewGroup(name, groupImageUrl);
    GroupMember groupLeader = GroupMember.createNewGroupLeader(userId, group.getGroupId());

    groupPort.save(group);
    groupMemberPort.save(groupLeader);
  }

  private void validateOwnership(String groupImageObjectKey, String userId) {
    fileOwnerCachePort.getOwnerId(groupImageObjectKey);

    String ownerId = fileOwnerCachePort.getOwnerId(groupImageObjectKey);
    fileOwnerCachePort.delete(groupImageObjectKey);

    if (!userId.equals(ownerId)) {
      throw new InvalidFileOwnerException();
    }
  }

}
