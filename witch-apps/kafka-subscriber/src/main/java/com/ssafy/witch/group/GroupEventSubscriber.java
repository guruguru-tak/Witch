package com.ssafy.witch.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.event.GroupEventTopic;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestApproveCommand;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestCommand;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestRejectCommand;
import com.ssafy.witch.group.event.ApproveGroupJoinRequestEvent;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import com.ssafy.witch.group.event.RejectGroupJoinRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GroupEventSubscriber {

  private final ObjectMapper objectMapper;
  private final NotifyGroupUseCase notifyGroupUseCase;

  @KafkaListener(topics = GroupEventTopic.GROUP_JOIN_REQUEST)
  public void handleGroupJoinRequestEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      CreateGroupJoinRequestEvent event = objectMapper.readValue(data.value(),
          CreateGroupJoinRequestEvent.class);

      notifyGroupUseCase.notifyJoinRequest(new NotifyGroupJoinRequestCommand(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @KafkaListener(topics = GroupEventTopic.GROUP_JOIN_REQUEST_APPROVE)
  public void handleApproveGroupJoinRequestEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      ApproveGroupJoinRequestEvent event = objectMapper.readValue(data.value(),
          ApproveGroupJoinRequestEvent.class);

      notifyGroupUseCase.notifyJoinRequestApproved(new NotifyGroupJoinRequestApproveCommand(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @KafkaListener(topics = GroupEventTopic.GROUP_JOIN_REQUEST_REJECT)
  public void handleRejectGroupJoinRequestEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      RejectGroupJoinRequestEvent event = objectMapper.readValue(data.value(),
          RejectGroupJoinRequestEvent.class);

      notifyGroupUseCase.notifyJoinRequestReject(new NotifyGroupJoinRequestRejectCommand(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }
}
