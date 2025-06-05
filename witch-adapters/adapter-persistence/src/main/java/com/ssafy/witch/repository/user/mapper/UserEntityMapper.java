package com.ssafy.witch.repository.user.mapper;

import com.ssafy.witch.entity.user.UserEntity;
import com.ssafy.witch.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  User toDomain(UserEntity userEntity);

  UserEntity toEntity(User user);

}
