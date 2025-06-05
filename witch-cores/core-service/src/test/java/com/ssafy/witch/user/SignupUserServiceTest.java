package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.exception.user.UserNicknameDuplicatedException;
import com.ssafy.witch.exception.user.UserEmailVerificationCodeNotValidException;
import com.ssafy.witch.user.command.SignupUserCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class SignupUserServiceTest {

    private SignupUserService sut;

    @Mock
    private ValidateUserUseCase validateUserUseCase;

    @Mock
    private VerifiedEmailCachePort verifiedEmailCachePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserPort userPort;

    @BeforeEach
    void setup() {
        sut = new SignupUserService(validateUserUseCase, verifiedEmailCachePort, passwordEncoder, userPort);
    }

    @Test
    void 이메일이_중복이_되면_회원가입이_실패한다() {
        // given
        SignupUserCommand command = new SignupUserCommand("test@test.com", "test12345", "testname", "123456");

        doThrow(new UserEmailDuplicatedException()).when(validateUserUseCase).checkUserEmailDuplication(command.getEmail());

        // when
        // then
        Assertions.assertThatThrownBy(() -> sut.signup(command))
                .isInstanceOf(UserEmailDuplicatedException.class);

        verify(userPort, never()).save(any(User.class)); // 유저 저장이 발생하지 않아야 함
    }

    @Test
    void 닉네임이_중복이_되면_회원가입이_실패한다() {
        // given
        SignupUserCommand command = new SignupUserCommand("test@test.com", "test12345", "testname", "123456");

        doThrow(new UserNicknameDuplicatedException()).when(validateUserUseCase).checkUserNicknameDuplication(command.getNickname());

        // when
        // then
        Assertions.assertThatThrownBy(() -> sut.signup(command))
                .isInstanceOf(UserNicknameDuplicatedException.class);

        verify(userPort, never()).save(any(User.class)); // 유저 저장이 발생하지 않아야 함
    }

    @Test
    void 이메일_인증_코드가_불일치하면_회원가입이_실패한다() {
        // given
        SignupUserCommand command = new SignupUserCommand("test@test.com", "test12345", "testname", "123456");

        BDDMockito.given(verifiedEmailCachePort.has(command.getEmail())).willReturn(false);

        // when
        // then
        Assertions.assertThatThrownBy(() -> sut.signup(command))
                .isInstanceOf(UserEmailVerificationCodeNotValidException.class);

        verify(userPort, never()).save(any(User.class)); // 유저 저장이 발생하지 않아야 함
    }

    @Test
    void 회원가입이_정상적으로_이루어지면_유저가_저장된다() {
        // given
        SignupUserCommand command = new SignupUserCommand("test@test.com", "test12345", "testname", "123456");

        // 이메일 & 닉네임 중복 검증 통과
        doNothing().when(validateUserUseCase).checkUserEmailDuplication(command.getEmail());
        doNothing().when(validateUserUseCase).checkUserNicknameDuplication(command.getNickname());

        // 이메일 인증 검증 - 인증됨
        BDDMockito.given(verifiedEmailCachePort.has(command.getEmail())).willReturn(true);
        BDDMockito.given(verifiedEmailCachePort.get(command.getEmail()))
                .willReturn(EmailVerificationCode.of(command.getEmailVerificationCode()));

        // 비밀번호 인코딩 Mock 설정
        BDDMockito.given(passwordEncoder.encode(command.getPassword())).willReturn("encodedPassword");

        // when
        sut.signup(command);

        // then
        verify(userPort, times(1)).save(any(User.class)); // 유저 저장이 정확히 한 번 호출되었는지 확인
    }
}
