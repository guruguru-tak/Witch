package com.ssafy.witch.mapper.user;

import com.ssafy.witch.entity.user.UserEntity;
import com.ssafy.witch.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserEntity toEntity(User user);

  User toDomain(UserEntity entity);

}
