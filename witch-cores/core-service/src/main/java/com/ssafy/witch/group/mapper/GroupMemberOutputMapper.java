package com.ssafy.witch.group.mapper;

import com.ssafy.witch.group.model.GroupMemberProjection;
import com.ssafy.witch.group.output.GroupMemberListOutput;
import com.ssafy.witch.group.output.GroupMemberOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMemberOutputMapper {

  default GroupMemberListOutput toOutput(List<GroupMemberProjection> projections) {
    return new GroupMemberListOutput(groupProjectionToOutputList(projections));
  }

  List<GroupMemberOutput> groupProjectionToOutputList(List<GroupMemberProjection> projections);
}
