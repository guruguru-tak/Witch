package com.ssafy.witch.user;

// 1. 중복일 경우 예외 처리하는지
// 2. 중복이 아닐 경우 넘어가는지

import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.only;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름을 한국어로 작성할 때 가독성이 높아짐.
@ExtendWith(  MockitoExtension.class) // Mockito 의 기능을 자동으로 활성화, @Mock 어노테이션으로 생성한 mock 객체들이 자동으로 주입.
class UserValidateEmailServiceTest {

    private UserValidateService sut; // 여기서 sut는 System Under Test 의 약어, 즉 테스트 대상 시스템으 나타낸다.
    // 나는 유저(이메일) 중복검증 테스트를 하려니까 UserValidateService

    @Mock
    private ValidateUserPort validateUserPort;

    @BeforeEach
    void setup() {
        sut = new UserValidateService(validateUserPort);
    }

    @Test
    void 이메일이_중복되면_예외처리를_발생시킨다() {
        //given
        String email = "test@test.com";

        BDDMockito.given(validateUserPort.isEmailDuplicated(email)).willReturn(true);

        //when
        //then
        Assertions.assertThatThrownBy(() -> sut.checkUserEmailDuplication(email)).isInstanceOf(UserEmailDuplicatedException.class);

        BDDMockito.then(validateUserPort).should(only()).isEmailDuplicated(email);
    }

    @Test
    void 이메일이_중복되지_않으면_예외처리가_발생하지_않는다() {
        //given
        String email = "test@test.com";

        BDDMockito.given(validateUserPort.isEmailDuplicated(email)).willReturn(false);

        //when
        //then
        Assertions.assertThatCode(() -> sut.checkUserEmailDuplication(email)).doesNotThrowAnyException();

        BDDMockito.then(validateUserPort).should(only()).isEmailDuplicated(email);

    }


}