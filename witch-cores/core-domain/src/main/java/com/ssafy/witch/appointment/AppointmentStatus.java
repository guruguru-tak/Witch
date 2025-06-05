package com.ssafy.witch.appointment;

import lombok.Getter;

@Getter
public enum AppointmentStatus {

  SCHEDULED("약속 전"),
  ONGOING("약속 진행 중"),
  FINISHED("약속 종료");

  private final String description;

  AppointmentStatus(String description) {
    this.description = description;
  }
}
