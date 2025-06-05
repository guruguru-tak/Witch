package com.ssafy.witch.group.mapper;

import com.ssafy.witch.group.model.GroupJoinRequestProjection;
import com.ssafy.witch.group.output.GroupJoinRequestListOutput;
import com.ssafy.witch.group.output.GroupJoinRequestOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupJoinRequestListOutputMapper {

  default GroupJoinRequestListOutput toOutput(List<GroupJoinRequestProjection> projections) {
    return new GroupJoinRequestListOutput(groupJoinRequestProjectionToOutputList(projections));
  }

  List<GroupJoinRequestOutput> groupJoinRequestProjectionToOutputList(
      List<GroupJoinRequestProjection> projections);
}
