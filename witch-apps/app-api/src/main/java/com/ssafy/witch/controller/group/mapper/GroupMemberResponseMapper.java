package com.ssafy.witch.controller.group.mapper;

import com.ssafy.witch.controller.group.response.GroupMemberListResponse;
import com.ssafy.witch.group.output.GroupMemberListOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMemberResponseMapper {

  GroupMemberListResponse toResponse(GroupMemberListOutput output);
}
