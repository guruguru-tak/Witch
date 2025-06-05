package com.ssafy.witch.appointment.output;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointMemberPositionListOutput {

  private List<AppointMemberPositionOutput> positions;

}
