package com.ssafy.witch.snack;

import com.ssafy.witch.snack.model.SnackDetailProjection;
import com.ssafy.witch.snack.model.SnackProjection;
import java.util.List;

public interface SnackReadPort {

  List<SnackProjection> getSnacks(String userId, String appointmentId);
}
