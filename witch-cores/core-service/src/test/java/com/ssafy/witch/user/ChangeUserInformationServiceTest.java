package com.ssafy.witch.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;

import com.ssafy.witch.exception.user.UserNicknameDuplicatedException;
import com.ssafy.witch.exception.user.UserNotFoundException;
import com.ssafy.witch.user.command.ChangeUserNicknameCommand;
import jakarta.validation.ConstraintViolationException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class ChangeUserInformationServiceTest {

  @InjectMocks
  private ChangeUserInformationService sut;

  @Mock
  private ValidateUserUseCase validateUserUseCase;
  @Mock
  private UserPort userPort;

  //정상 작동과 예외케이스 테스트
  // 닉네임 포맷 틀린거 -> 커맨드 테스트로 분리
  //1. 닉네임 중복 예외
  //2. id로 검색했는데 없는 유저인 경우 예외처리
  //3. userPort로 저장하는 과정에서 에러 찾아보기 -> 없음 -> 닉네임 중복 아니고 있는 유저면 테스트 통과
  //3-1. 이전 닉네임과 변경후 닉네임이 다른지도 확인?
  @Test
  void 닉네임이_중복인_경우_닉네임을_변경할_수_없다() {
    //given
    String nickname = "test";
    BDDMockito
        .willThrow(UserNicknameDuplicatedException.class)
        .given(validateUserUseCase).checkUserNicknameDuplication(nickname);
    //when&then
    Assertions.assertThatThrownBy(()->{
      //when
      ChangeUserNicknameCommand command = new ChangeUserNicknameCommand("1", nickname);
      sut.changeUserNickname(command);
      //then
    }).isInstanceOf(UserNicknameDuplicatedException.class);
  }

  @Test
  void 존재하지_않는_유저는_id로_검색할_수_없다() {
    //given
    BDDMockito
        .given(userPort.findById("1"))
        .willReturn(Optional.empty());
    //when&then
    Assertions.assertThatThrownBy(() -> {
      ChangeUserNicknameCommand command = new ChangeUserNicknameCommand("1", "aaaaaaa");
      sut.changeUserNickname(command);
    }).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 닉네임이_중복이_아니고_존재하는_유저이면_닉네임_변경에_성공한다() {
    //given
    User fakeUser = new User("1","test@test.com","ddddd!!@A","aaaaaa",null);
    BDDMockito
        .given(userPort.findById("1"))
        .willReturn(Optional.of(fakeUser));
    //when&then
    Assertions.assertThatCode(() ->{
      ChangeUserNicknameCommand command = new ChangeUserNicknameCommand("1", "abcdefg");
      sut.changeUserNickname(command);
    }).doesNotThrowAnyException();

    //닉네임 변경이 되는지 확인
    Assertions.assertThat(fakeUser.getNickname()).isEqualTo("abcdefg");
    //only 빼니까 잘 돌아감
    BDDMockito.then(userPort).should().save(fakeUser);
  }
}