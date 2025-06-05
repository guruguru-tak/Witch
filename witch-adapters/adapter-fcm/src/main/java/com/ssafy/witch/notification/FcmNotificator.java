package com.ssafy.witch.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmNotificator {

  public void sendNotification(WitchNotification witchNotification) {
    try {
      Message message = witchNotification.toMessage();
      FirebaseMessaging.getInstance().send(message);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
