package com.ssafy.witch.appointment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.apoointment.event.AppointmentArrivalEvent;
import com.ssafy.witch.apoointment.event.AppointmentCreatedEvent;
import com.ssafy.witch.apoointment.event.AppointmentEndEvent;
import com.ssafy.witch.apoointment.event.AppointmentExitEvent;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.apoointment.event.AppointmentStartEvent;
import com.ssafy.witch.appointment.command.NotifyAppointmentArrivalCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentCreatedCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentEndCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentExitCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentStartCommand;
import com.ssafy.witch.event.AppointmentEventTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointEventSubscriber {

  private final ObjectMapper objectMapper;
  private final NotifyAppointmentUseCase notifyAppointmentUseCase;

  @KafkaListener(topics = AppointmentEventTopic.JOIN_APPOINTMENT)
  public void handleAppointmentJoinEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      AppointmentJoinEvent appointmentJoinEvent = objectMapper.readValue(data.value(),
          AppointmentJoinEvent.class);

      notifyAppointmentUseCase.notifyJoin(new NotifyAppointmentJoinCommand(appointmentJoinEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @KafkaListener(topics = AppointmentEventTopic.START_APPOINTMENT)
  public void handleAppointmentStartEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      AppointmentStartEvent appointmentStartEvent = objectMapper.readValue(data.value(),
          AppointmentStartEvent.class);

      notifyAppointmentUseCase.notifyStart(
          new NotifyAppointmentStartCommand(appointmentStartEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @KafkaListener(topics = AppointmentEventTopic.END_APPOINTMENT)
  public void handleAppointmentEndEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      AppointmentEndEvent appointmentEndEvent = objectMapper.readValue(data.value(),
          AppointmentEndEvent.class);

      notifyAppointmentUseCase.notifyEnd(
          new NotifyAppointmentEndCommand(appointmentEndEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @KafkaListener(topics = AppointmentEventTopic.ARRIVAL_APPOINTMENT)
  public void handleAppointmentArrivalEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      AppointmentArrivalEvent appointmentArrivalEvent = objectMapper.readValue(data.value(),
          AppointmentArrivalEvent.class);

      notifyAppointmentUseCase.notifyArrival(
          new NotifyAppointmentArrivalCommand(appointmentArrivalEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @KafkaListener(topics = AppointmentEventTopic.APPOINTMENT_CREATED)
  public void handleAppointmentCreatedEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      AppointmentCreatedEvent event = objectMapper.readValue(data.value(),
          AppointmentCreatedEvent.class);

      notifyAppointmentUseCase.notifyCreated(
          new NotifyAppointmentCreatedCommand(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @KafkaListener(topics = AppointmentEventTopic.APPOINTMENT_EXIT)
  public void handleAppointmentExitEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      AppointmentExitEvent event = objectMapper.readValue(data.value(),
          AppointmentExitEvent.class);

      notifyAppointmentUseCase.notifyExit(
          new NotifyAppointmentExitCommand(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }
}
