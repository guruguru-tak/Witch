package com.ssafy.witch.group.command;

import java.util.Locale;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GroupLeaveCommandTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void 유저_ID가_비어있으면_커맨드를_생성할_수_없다() {
        // given
        String userId = null;
        String groupId = "group123";
        LeaveGroupCommand command = new LeaveGroupCommand(userId, groupId);

        // when
        var violations = validator.validate(command);

        // then
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }

    @Test
    void 그룹_ID가_비어있으면_커맨드를_생성할_수_없다() {
        // given
        String userId = "user123";
        String groupId = null;
        LeaveGroupCommand command = new LeaveGroupCommand(userId, groupId);

        // when
        var violations = validator.validate(command);

        // then
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
}
