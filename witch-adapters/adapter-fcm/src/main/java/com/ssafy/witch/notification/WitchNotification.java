package com.ssafy.witch.notification;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WitchNotification {

  Map<String, String> data;
  private String token;
  private String title;
  private String body;

  public Message toMessage() {
    Notification notification = Notification.builder()
        .setTitle(title)
        .setBody(body)
        .build();
    return Message.builder()
        .setToken(token)
        .setNotification(notification)
        .putAllData(data)
        .build();
  }
}
