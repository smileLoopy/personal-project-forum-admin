package com.personal.projectforumadmin.controller;

import com.personal.projectforumadmin.config.TestSecurityConfig;
import com.personal.projectforumadmin.dto.PostingDto;
import com.personal.projectforumadmin.dto.UserAccountDto;
import com.personal.projectforumadmin.service.PostingManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName(("Controller - Posting Management"))
@Import(TestSecurityConfig.class)
@WebMvcTest(PostingManagementController.class)
class PostingManagementControllerTest {

    private final MockMvc mvc;

    @MockBean private PostingManagementService postingManagementService;

    public PostingManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[view][GET] Posting Management Page - Normal  Retrieval")
    @Test
    void givenNothing_whenRequestingPostingManagementView_thenReturnsPostingManagementView() throws Exception {
        // Given
        given(postingManagementService.getPostings()).willReturn(List.of());
        // When & Then
        mvc.perform(get("/management/postings"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/postings"))
                .andExpect(model().attribute("postings", List.of()));
        then(postingManagementService).should().getPostings();;
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] one posting - normal retrieval")
    @Test
    void givenPostingId_whenRequestingPosting_thenReturnsPosting() throws Exception {
        // Given
        Long postingId = 1L;
        PostingDto postingDto = createPostingDto("title", "content");
        given(postingManagementService.getPosting(postingId)).willReturn(postingDto);

        // When & Then
        mvc.perform(get("/management/postings/" + postingId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postingId))
                .andExpect(jsonPath("$.title").value(postingDto.title()))
                .andExpect(jsonPath("$.content").value(postingDto.content()))
                .andExpect(jsonPath("$.userAccount.nickname").value(postingDto.userAccount().nickname()));
        then(postingManagementService).should().getPosting(postingId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[view][POST] delete posting - normal retrieval")
    @Test
    void givenPostingId_whenRequestingDeletion_thenRedirectsToPostingManagementView() throws Exception {
        // Given
        Long postingId = 1L;
        willDoNothing().given(postingManagementService).deletePosting(postingId);

        // When & Then
        mvc.perform(
                        post("/management/postings/" + postingId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/postings"))
                .andExpect(redirectedUrl("/management/postings"));
        then(postingManagementService).should().deletePosting(postingId);
    }


    private PostingDto createPostingDto(String title, String content) {
        return PostingDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Eunah",
                LocalDateTime.now(),
                "Eunah"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "eunahTest",
                "eunah-test@email.com",
                "eunah-test",
                "test memo"
        );
    }

}