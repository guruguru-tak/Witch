package com.ssafy.witch.mapper.group;

import com.ssafy.witch.entity.group.GroupMemberEntity;
import com.ssafy.witch.group.GroupMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMemberMapper {


  GroupMemberEntity toEntity(GroupMember groupMember);

  GroupMember toDomain(GroupMemberEntity groupMemberEntity);
}
