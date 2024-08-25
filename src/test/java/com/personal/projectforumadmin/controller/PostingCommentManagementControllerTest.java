package com.personal.projectforumadmin.controller;

import com.personal.projectforumadmin.config.SecurityConfig;
import com.personal.projectforumadmin.domain.constant.RoleType;
import com.personal.projectforumadmin.dto.PostingCommentDto;
import com.personal.projectforumadmin.dto.UserAccountDto;
import com.personal.projectforumadmin.service.PostingCommentManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Controller - Comment Management")
@Import(SecurityConfig.class)
@WebMvcTest(PostingCommentManagementController.class)
class PostingCommentManagementControllerTest {

    private final MockMvc mvc;

    @MockBean private PostingCommentManagementService postingCommentManagementService;

    public PostingCommentManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] Comment Management Page - Normal Retrieval")
    @Test
    void givenNothing_whenRequestingPostingCommentManagementView_thenReturnsPostingCommentManagementView() throws Exception {
        // Given
        given(postingCommentManagementService.getPostingComments()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/management/posting-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/posting-comments"))
                .andExpect(model().attribute("comments", List.of()));
        then(postingCommentManagementService).should().getPostingComments();
    }

    @DisplayName("[data][GET] Single Comment - Normal Retrieval")
    @Test
    void givenCommentId_whenRequestingPostingComment_thenReturnsPostingComment() throws Exception {
        // Given
        Long postingCommentId = 1L;
        PostingCommentDto postingCommentDto = createPostingCommentDto("comment");
        given(postingCommentManagementService.getPostingComment(postingCommentId)).willReturn(postingCommentDto);

        // When & Then
        mvc.perform(get("/management/posting-comments/" + postingCommentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postingCommentId))
                .andExpect(jsonPath("$.content").value(postingCommentDto.content()))
                .andExpect(jsonPath("$.userAccount.nickname").value(postingCommentDto.userAccount().nickname()));
        then(postingCommentManagementService).should().getPostingComment(postingCommentId);
    }

    @DisplayName("[view][POST] Delete Comment - Normal Retrieval")
    @Test
    void givenCommentId_whenRequestingDeletion_thenRedirectsToPostingCommentManagementView() throws Exception {
        // Given
        Long postingCommentId = 1L;
        willDoNothing().given(postingCommentManagementService).deletePostingComment(postingCommentId);

        // When & Then
        mvc.perform(
                        post("/management/posting-comments/" + postingCommentId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/posting-comments"))
                .andExpect(redirectedUrl("/management/posting-comments"));
        then(postingCommentManagementService).should().deletePostingComment(postingCommentId);
    }


    private PostingCommentDto createPostingCommentDto(String content) {
        return PostingCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                null,
                content,
                LocalDateTime.now(),
                "Eunah",
                LocalDateTime.now(),
                "Eunah"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "eunahTest",
                "pw",
                Set.of(RoleType.ADMIN),
                "eunah-test@email.com",
                "eunah-test",
                "test memo"
        );
    }

}