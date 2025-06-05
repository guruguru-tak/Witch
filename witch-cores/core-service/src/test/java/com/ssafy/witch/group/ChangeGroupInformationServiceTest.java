package com.ssafy.witch.group;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import com.ssafy.witch.exception.group.GroupNameDuplicatedException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.ChangeGroupNameCommand;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class ChangeGroupInformationServiceTest {

  @InjectMocks
  private ChangeGroupInformationService sut;

  @Mock
  private GroupValidateUseCase groupValidateUseCase;

  @Mock
  private GroupPort groupPort;

  @Mock
  private GroupMemberPort groupMemberPort;

  //1. 존재하지 않는 그룹인 경우
  //2. 권한이 없는 경우
  //3. 변경할 모임 이름이 중복인 경우
  //4. 정상 저장
  @Test
  void 존재하지_않는_그룹의_이름을_변경할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String name = "testname";

    BDDMockito
        .given(groupPort.findById(any()))
        .willReturn(Optional.empty());

    //when%then
    Assertions.assertThatThrownBy(() -> {
      ChangeGroupNameCommand command = new ChangeGroupNameCommand(userId, groupId, name);
      sut.changeGroupName(command);
    }).isInstanceOf(GroupNotFoundException.class);
  }

  @Test
  void 그룹장이_아닌_그룹의_이름을_변경할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String name = "testname";
    Group group = new Group(groupId, "any", "any");

    BDDMockito
        .given(groupPort.findById(groupId))
        .willReturn(Optional.of(group));

    BDDMockito
        .given(groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId))
        .willReturn(false);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      ChangeGroupNameCommand command = new ChangeGroupNameCommand(userId, groupId, name);
      sut.changeGroupName(command);
    }).isInstanceOf(UnauthorizedGroupAccessException.class);
  }

  @Test
  void 변경할_그룹_이름이_중복인_경우_변경할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String name = "testname";
    Group group = new Group(groupId, "any", "any");

    BDDMockito
        .given(groupPort.findById(groupId))
        .willReturn(Optional.of(group));

    BDDMockito
        .given(groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId))
        .willReturn(true);

    BDDMockito
        .willThrow(GroupNameDuplicatedException.class)
        .given(groupValidateUseCase).checkGroupNameDuplication(name);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      ChangeGroupNameCommand command = new ChangeGroupNameCommand(userId, groupId, name);
      sut.changeGroupName(command);
    }).isInstanceOf(GroupNameDuplicatedException.class);
  }


  @Test
  void 그룹_이름_변경에_성공한다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String name = "testname";
    Group group = new Group(groupId, "any", "any");

    BDDMockito
        .given(groupPort.findById(groupId))
        .willReturn(Optional.of(group));

    BDDMockito
        .given(groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId))
        .willReturn(true);

    //when&then
    Assertions.assertThatCode(() -> {
      ChangeGroupNameCommand command = new ChangeGroupNameCommand(userId, groupId, name);
      sut.changeGroupName(command);
    }).doesNotThrowAnyException();


    then(groupPort).should().save(group);
  }
}