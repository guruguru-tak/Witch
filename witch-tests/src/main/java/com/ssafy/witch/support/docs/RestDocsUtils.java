package com.ssafy.witch.support.docs;

import org.springframework.restdocs.snippet.Attributes.Attribute;

public class RestDocsUtils {

  private RestDocsUtils() {

  }

  public static Attribute constraints( // constraints Attribute 간단하게 추가
      final String value) {
    return new Attribute("constraints", value);
  }

}
