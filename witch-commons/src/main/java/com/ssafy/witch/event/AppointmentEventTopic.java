package com.ssafy.witch.event;

public class AppointmentEventTopic {

  public final static String JOIN_APPOINTMENT = "APPOINTMENT_JOIN";
  public static final String START_APPOINTMENT = "APPOINTMENT_START";
  public static final String END_APPOINTMENT = "APPOINTMENT_END";
  public static final String ARRIVAL_APPOINTMENT = "APPOINTMENT_ARRIVAL";
  public static final String APPOINTMENT_CREATED = "APPOINTMENT_CREATED";
  public static final String APPOINTMENT_EXIT = "APPOINTMENT_EXIT";

  private AppointmentEventTopic() {
  }
}
