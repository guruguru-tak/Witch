package com.ssafy.witch.mapper.group;

import com.ssafy.witch.entity.group.GroupJoinRequestEntity;
import com.ssafy.witch.group.GroupJoinRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupJoinRequestMapper {

  GroupJoinRequestEntity toEntity(GroupJoinRequest domain);

  GroupJoinRequest toDomain(GroupJoinRequestEntity entity);

}
