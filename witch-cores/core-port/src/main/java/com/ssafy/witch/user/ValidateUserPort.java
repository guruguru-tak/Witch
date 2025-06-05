package com.ssafy.witch.user;

public interface ValidateUserPort {

  boolean isEmailDuplicated(String email);

  boolean isNicknameDuplicated(String nickname);
}
