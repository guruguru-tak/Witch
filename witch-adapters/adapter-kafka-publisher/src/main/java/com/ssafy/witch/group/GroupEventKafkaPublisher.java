package com.ssafy.witch.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.event.GroupEventTopic;
import com.ssafy.witch.group.event.ApproveGroupJoinRequestEvent;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import com.ssafy.witch.group.event.RejectGroupJoinRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GroupEventKafkaPublisher implements GroupEventPublishPort {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;


  @Override
  public void publish(CreateGroupJoinRequestEvent event) {
    try {
      kafkaTemplate.send(GroupEventTopic.GROUP_JOIN_REQUEST,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void publish(ApproveGroupJoinRequestEvent event) {
    try {
      kafkaTemplate.send(GroupEventTopic.GROUP_JOIN_REQUEST_APPROVE,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void publish(RejectGroupJoinRequestEvent event) {
    try {
      kafkaTemplate.send(GroupEventTopic.GROUP_JOIN_REQUEST_REJECT,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }
}
