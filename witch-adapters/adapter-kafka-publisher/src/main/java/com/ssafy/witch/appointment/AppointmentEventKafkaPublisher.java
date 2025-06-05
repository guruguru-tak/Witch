package com.ssafy.witch.appointment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.apoointment.AppointmentEventPublishPort;
import com.ssafy.witch.apoointment.event.AppointmentArrivalEvent;
import com.ssafy.witch.apoointment.event.AppointmentCreatedEvent;
import com.ssafy.witch.apoointment.event.AppointmentEndEvent;
import com.ssafy.witch.apoointment.event.AppointmentExitEvent;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.apoointment.event.AppointmentStartEvent;
import com.ssafy.witch.event.AppointmentEventTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointmentEventKafkaPublisher implements AppointmentEventPublishPort {


  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void publish(AppointmentJoinEvent appointmentJoinEvent) {
    try {
      kafkaTemplate.send(AppointmentEventTopic.JOIN_APPOINTMENT,
          objectMapper.writeValueAsString(appointmentJoinEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void publish(AppointmentStartEvent appointmentStartEvent) {

    try {
      kafkaTemplate.send(AppointmentEventTopic.START_APPOINTMENT,
          objectMapper.writeValueAsString(appointmentStartEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void publish(AppointmentEndEvent event) {
    try {
      kafkaTemplate.send(AppointmentEventTopic.END_APPOINTMENT,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void publish(AppointmentArrivalEvent event) {
    try {
      kafkaTemplate.send(AppointmentEventTopic.ARRIVAL_APPOINTMENT,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void publish(AppointmentCreatedEvent event) {
    try {
      kafkaTemplate.send(AppointmentEventTopic.APPOINTMENT_CREATED,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void publish(AppointmentExitEvent event) {
    try {
      kafkaTemplate.send(AppointmentEventTopic.APPOINTMENT_EXIT,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }

  }
}
