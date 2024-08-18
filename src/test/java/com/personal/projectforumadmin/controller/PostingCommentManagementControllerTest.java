package com.personal.projectforumadmin.controller;

import com.personal.projectforumadmin.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View Controller - Comment Management")
@Import(SecurityConfig.class)
@WebMvcTest(PostingCommentManagementController.class)
class PostingCommentManagementControllerTest {

    private final MockMvc mvc;

    public PostingCommentManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] Comment Management Page - Normal Retrieval")
    @Test
    void givenNothing_whenRequestingPostingCommentManagementView_thenReturnsPostingCommentManagementView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/management/posting-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/postingComments"));
    }

}