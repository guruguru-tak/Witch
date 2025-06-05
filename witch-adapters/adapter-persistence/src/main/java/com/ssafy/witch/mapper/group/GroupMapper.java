package com.ssafy.witch.mapper.group;

import com.ssafy.witch.entity.group.GroupEntity;
import com.ssafy.witch.group.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

  GroupEntity toEntity(Group group);

  Group toDomain(GroupEntity groupEntity);
}
