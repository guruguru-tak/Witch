package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserNicknameDuplicatedException;
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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserValidateNicknameServiceTest {

    private UserValidateService sut;

    @Mock
    private ValidateUserPort validateUserPort;

    @BeforeEach
    void setUp() {
        sut = new UserValidateService(validateUserPort);
    }

    @Test
    void 닉네임이_중복되면_예외처리를_발생시킨다() {
        //given
        String nickname = "test";

        BDDMockito.given(validateUserPort.isNicknameDuplicated(nickname)).willReturn(true);

        //when
        //then
        Assertions.assertThatThrownBy(() -> sut.checkUserNicknameDuplication(nickname)).isInstanceOf(UserNicknameDuplicatedException.class);

        BDDMockito.then(validateUserPort).should(only()).isNicknameDuplicated(nickname);
    }

    @Test
    void 닉네임이_중복되지_않으면_예외처리를_발생시키지_않는다() {
        //given
        String nickname = "test";

        BDDMockito.given(validateUserPort.isNicknameDuplicated(nickname)).willReturn(false);

        //when
        //then
        Assertions.assertThatCode(() -> sut.checkUserNicknameDuplication(nickname)).doesNotThrowAnyException();

        BDDMockito.then(validateUserPort).should(only()).isNicknameDuplicated(nickname);
    }
}
