package com.ssafy.witch.controller.snack.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackListResponse {

  private List<SnackResponse> snacks;
}
