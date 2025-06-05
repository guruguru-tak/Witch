package com.ssafy.witch.snack.mapper;

import com.ssafy.witch.snack.model.SnackDetailProjection;
import com.ssafy.witch.snack.model.SnackProjection;
import com.ssafy.witch.snack.output.SnackDetailOutput;
import com.ssafy.witch.snack.output.SnackListOutput;
import com.ssafy.witch.snack.output.SnackOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnackOutputMapper {

  default SnackListOutput toOutput(List<SnackProjection> projections) {
    return new SnackListOutput(snackProjectionToOutputList(projections));
  }

  List<SnackOutput> snackProjectionToOutputList(
      List<SnackProjection> projections);
}
