package com.ssafy.witch.user;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.willThrow;

import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.exception.user.UserEmailVerificationCodeNotValidException;
import com.ssafy.witch.user.command.VerifyUserEmailVerificationCodeCommand;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class VerifyUserEmailServiceTest {

  @InjectMocks
  private VerifyUserEmailService sut;

  @Mock
  private ValidateUserUseCase validateUserUseCase;
  @Mock
  private EmailVerificationCodeCachePort emailVerificationCodeCachePort;
  @Mock
  private VerifiedEmailCachePort verifiedEmailCachePort;

  //1. 이메일 인증을 요청한 이메일이 중복인 경우 예외 발생
  //2. 이메일 인증을 요청한 이메일과 다른 경우 예외 발생
  //3. 발급한 이메일 인증 코드와 다른 경우 예외 발생
  //4, 정상 작동 검증

  @Test
  void 이메일_인증을_요청한_이메일이_중복인_경우_인증_코드를_발급할_수_없다() {
    //given
    String email = "test@test.com";
    willThrow(UserEmailDuplicatedException.class)
        .given(validateUserUseCase).checkUserEmailDuplication(email);
    //when&then
    Assertions.assertThatThrownBy(() -> {
      VerifyUserEmailVerificationCodeCommand command =
          new VerifyUserEmailVerificationCodeCommand(email, "123456");
      sut.verifyEmailVerificationCode(command);
    }).isInstanceOf(UserEmailDuplicatedException.class);
  }

  @Test
  void 이메일_인증을_요청한_내역이_없는_경우_인증에_실패한다() {
    //given
    String email = "test@test.com";
    String code = "123456";
    //when&then
    Assertions.assertThatThrownBy(() -> {
      VerifyUserEmailVerificationCodeCommand command =
          new VerifyUserEmailVerificationCodeCommand(email, code);
      sut.verifyEmailVerificationCode(command);
    }).isInstanceOf(UserEmailVerificationCodeNotValidException.class);
  }

  @Test
  void 발급한_이메일_인증_코드와_값이_다른_경우_인증에_실패한다() {
    //given
    //1. emailVerificationCodeCachePort에 인증을 요청한 이메일과 코드 존재
    //2.
    String email = "test@test.com";
    String code = "123456";
    given(emailVerificationCodeCachePort.get(email))
        .willReturn(EmailVerificationCode.of(code));

    given(emailVerificationCodeCachePort.has(email))
        .willReturn(true);
    //when&then
    Assertions.assertThatThrownBy(() -> {
      VerifyUserEmailVerificationCodeCommand command =
          new VerifyUserEmailVerificationCodeCommand(email, "111131");
      sut.verifyEmailVerificationCode(command);
    }).isInstanceOf(UserEmailVerificationCodeNotValidException.class);
  }

  @Test
  void 발급받은_이메일_인증_코드로_인증할_수_있다() {

    //given
    String email = "test@test.com";
    String code = "123456";
    EmailVerificationCode emailVerificationCode = EmailVerificationCode.of(code);
    given(emailVerificationCodeCachePort.get(email))
        .willReturn(EmailVerificationCode.of(code));

    given(emailVerificationCodeCachePort.has(email))
        .willReturn(true);

    //when&then
    Assertions.assertThatCode(()->{
      VerifyUserEmailVerificationCodeCommand command =
          new VerifyUserEmailVerificationCodeCommand(email, code);
      sut.verifyEmailVerificationCode(command);
    }).doesNotThrowAnyException();

    then(emailVerificationCodeCachePort).should(times(1)).remove(email);
    then(verifiedEmailCachePort).should(times(1)).upsert(eq(email), eq(emailVerificationCode), any(Duration.class));
  }
}