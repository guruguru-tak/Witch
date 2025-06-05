package com.ssafy.witch.snack.output;

import java.util.List;
import lombok.Getter;

@Getter
public class SnackListOutput {

  private final List<SnackOutput> snacks;

  public SnackListOutput(List<SnackOutput> snacks) {
    this.snacks = snacks;
  }
}
