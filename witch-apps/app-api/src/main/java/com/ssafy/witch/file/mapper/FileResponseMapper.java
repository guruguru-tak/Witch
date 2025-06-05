package com.ssafy.witch.file.mapper;

import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.response.PresignedUrlResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileResponseMapper {

  PresignedUrlResponse toResponse(PresignedUrl presignedUrl);

}
