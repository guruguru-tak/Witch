package com.ssafy.witch.apoointment;

import com.ssafy.witch.appointment.Position;

public interface AppointmentMemberPositionCachePort {


  void upsert(String userId, Position position);

  Position get(String userId);
}