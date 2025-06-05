package com.ssafy.witch.controller.snack;

import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.snack.mapper.SnackRequestMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.file.UnsupportedFileFormatException;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.mapper.FileRequestMapper;
import com.ssafy.witch.file.mapper.FileResponseMapper;
import com.ssafy.witch.file.request.PresignedUrlRequest;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.snack.SnackFileUseCase;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({SnackImageController.class, FileRequestMapper.class, FileResponseMapper.class,
    SnackRequestMapper.class})
class SnackImageControllerTest extends RestDocsTestSupport {

  @MockBean
  private SnackFileUseCase snackFileUseCase;

  @WithMockWitchUser
  @Test
  void generate_snack_image_presigned_url_200() throws Exception {

    PresignedUrlRequest request = new PresignedUrlRequest("snack-image.jpeg");
    String presignedUrl = "http://presigned.url.example";
    String objectKey = "snack/object/key/example";

    given(snackFileUseCase.generateSnackFilePresignedUrl(any()))
        .willReturn(new PresignedUrl(presignedUrl, objectKey));

    mvc.perform(post("/snacks/snack-image/presigned-url")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer sample.access.token")
        .content(objectMapper.writeValueAsString(request))
    )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            requestFields(
                fieldWithPath("fileName")
                    .type(STRING)
                    .description("업로드할 파일명")
                    .attributes(constraints("업로드할 파일의 확장자가 jpg, jpeg 또는 png여야 합니다."))
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.presignedUrl")
                    .type(STRING)
                    .description("PUT으로 해당 URL에 파일 업로드"),
                fieldWithPath("data.objectKey")
                    .type(STRING)
                    .description("업로드할 파일의 object key")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void generate_snack_image_presigned_url_400_unsupported_file_format() throws Exception {

    PresignedUrlRequest request = new PresignedUrlRequest("snack-image.jpeg");

    BDDMockito
        .given(snackFileUseCase.generateSnackFilePresignedUrl(any()))
        .willThrow(new UnsupportedFileFormatException());

    mvc.perform(post("/snacks/snack-image/presigned-url")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNSUPPORTED_FILE_FORMAT.getErrorCode()))
        .andDo(restDocs.document());
  }

}