package com.ssafy.witch.controller.user.mapper;

import com.ssafy.witch.controller.user.response.UserDetailResponse;
import com.ssafy.witch.controller.user.response.UserResponse;
import com.ssafy.witch.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

  UserResponse toResponse(User user);

  UserDetailResponse toDetailResponse(User user);
}
