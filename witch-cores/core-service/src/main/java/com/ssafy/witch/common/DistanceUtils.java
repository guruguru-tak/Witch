package com.ssafy.witch.common;

import com.ssafy.witch.appointment.Position;

public class DistanceUtils {

  private static final double EARTH_RADIUS = 6371000;

  private DistanceUtils() {
  }

  public static double getDistance(Position from, Position to) {
    // 위도와 경도를 라디안 단위로 변환
    double lat1 = from.getLatitude();
    double lon1 = from.getLongitude();
    double lat2 = to.getLatitude();
    double lon2 = to.getLongitude();

    double radLat1 = Math.toRadians(lat1);
    double radLat2 = Math.toRadians(lat2);
    double deltaLat = Math.toRadians(lat2 - lat1);
    double deltaLon = Math.toRadians(lon2 - lon1);

    // Haversine 공식 적용
    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
        + Math.cos(radLat1) * Math.cos(radLat2)
        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    // 두 점 사이의 거리 계산 (미터 단위)
    return Math.abs(EARTH_RADIUS * c);
  }

}
