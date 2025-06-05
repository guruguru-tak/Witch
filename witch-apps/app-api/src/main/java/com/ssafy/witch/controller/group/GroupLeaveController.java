package com.ssafy.witch.controller.group;

import com.ssafy.witch.group.LeaveGroupUseCase;
import com.ssafy.witch.group.command.LeaveGroupCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupLeaveController {

    private final LeaveGroupUseCase leaveGroupUseCase;

    @DeleteMapping("/groups/{groupId}/members/me")
    public WitchApiResponse<Void> leaveGroup(
            @AuthenticationPrincipal String userId,
            @PathVariable String groupId) {
        LeaveGroupCommand command = new LeaveGroupCommand(userId, groupId);
        leaveGroupUseCase.leaveGroup(command);

        return WitchApiResponse.success();
    }
}
