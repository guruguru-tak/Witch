package com.ssafy.witch.group.command;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LeaveGroupCommand {

    @NotNull
    private final String UserId;

    @NotNull
    private final String GroupId;
}
