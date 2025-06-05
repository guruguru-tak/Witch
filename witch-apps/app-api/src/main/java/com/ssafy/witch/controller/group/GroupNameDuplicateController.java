package com.ssafy.witch.controller.group;

import com.ssafy.witch.group.GroupValidateUseCase;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupNameDuplicateController {

  private final GroupValidateUseCase groupValidateUseCase;

  @GetMapping("/groups/name/is-unique")
  public WitchApiResponse<Void> checkGroupNameUnique(
      @RequestParam("name") String name) {

    groupValidateUseCase.checkGroupNameDuplication(name);
    return WitchApiResponse.success();
  }
}
