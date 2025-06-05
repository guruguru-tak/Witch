package com.ssafy.witch.file;

import java.time.Duration;

public interface FileOwnerCachePort {

  void save(String objectKey, String ownerId, Duration ttl);

  String getOwnerId(String objectKey);

  void delete(String objectKey);
}
