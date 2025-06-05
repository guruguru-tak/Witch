package com.ssafy.witch.controller.snack;

import static org.mockito.Mockito.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.snack.SnackNotFoundException;
import com.ssafy.witch.exception.snack.UnauthorizedSnackAccessException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.snack.DeleteSnackUseCase;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(SnackDeleteController.class)
class SnackDeleteControllerTest extends RestDocsTestSupport {

  @MockBean
  private DeleteSnackUseCase deleteSnackUseCase;

  @WithMockWitchUser
  @Test
  void delete_snack_200() throws Exception {

    String snackId = "snack-id-example";

    mvc.perform(delete("/snack/{snackId}", snackId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("snackId")
                    .description("삭제할 스낵 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access Token")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void delete_snack_400_snack_not_exists() throws Exception {

    String snackId = "snack-id-example";

    BDDMockito
        .doThrow(new SnackNotFoundException())
        .when(deleteSnackUseCase).deleteSnack(any());

    mvc.perform(delete("/snack/{snackId}", snackId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NON_EXISTENT_SNACK.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void delete_snack_400_user_is_not_snack_owner() throws Exception {

    String snackId = "snack-id-example";

    BDDMockito
        .doThrow(new UnauthorizedSnackAccessException())
        .when(deleteSnackUseCase).deleteSnack(any());

    mvc.perform(delete("/snack/{snackId}", snackId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_SNACK_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }
}