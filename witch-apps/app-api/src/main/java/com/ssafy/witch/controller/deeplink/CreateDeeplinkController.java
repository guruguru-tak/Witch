package com.ssafy.witch.controller.deeplink;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateDeeplinkController {

  @Value("${witch.app.deeplink}")
  private String appDeeplink;

  @GetMapping("/deeplink")
  public ResponseEntity<Void> createDeeplink(@RequestParam String groupId) {
    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, String.format(appDeeplink, groupId))
        .build();
  }
}
