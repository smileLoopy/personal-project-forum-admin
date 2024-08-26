package com.personal.projectforumadmin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.projectforumadmin.domain.constant.RoleType;
import com.personal.projectforumadmin.dto.PostingCommentDto;
import com.personal.projectforumadmin.dto.UserAccountDto;
import com.personal.projectforumadmin.dto.properties.ProjectProperties;
import com.personal.projectforumadmin.dto.response.PostingCommentClientResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("Business Logic - Comment Management")
class PostingCommentManagementServiceTest {

    @Disabled("Only for checking real API response result, thus disabled in normal test")
    @DisplayName("Real API Retrieval Test")
    @SpringBootTest
    @Nested
    class RealApiTest {

        private final PostingCommentManagementService sut;

        @Autowired
        public RealApiTest(PostingCommentManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("Retrieval Comment API, return Comments.")
        @Test
        void givenNothing_whenCallingCommentApi_thenReturnsCommentList() {
            // Given

            // When
            List<PostingCommentDto> result = sut.getPostingComments();

            // Then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }
    }

    @DisplayName("API mocking Test")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(PostingCommentManagementService.class)
    @Nested
    class RestTemplateTest {

        private final PostingCommentManagementService sut;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(
                PostingCommentManagementService sut,
                ProjectProperties projectProperties,
                MockRestServiceServer server,
                ObjectMapper mapper

        ) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("Retrieval comment list API, return comments.")
        @Test
        void givenNothing_whenCallingCommentsApi_thenReturnsCommentList() throws Exception {
            // Given
            PostingCommentDto expectedComment = createPostingCommentDto("댓글");
            PostingCommentClientResponse expectedResponse = PostingCommentClientResponse.of(List.of(expectedComment));
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/postingComments?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));


            // When
            List<PostingCommentDto> result = sut.getPostingComments();

            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedComment.id())
                    .hasFieldOrPropertyWithValue("content", expectedComment.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.userAccount().nickname());
            server.verify();
        }

        @DisplayName("Retrieval comment API with commentId, return comment.")
        @Test
        void givenCommentId_whenCallingCommentApi_thenReturnsComment() throws Exception {
            // Given
            Long postingCommentId = 1L;
            PostingCommentDto expectedComment = createPostingCommentDto("comment");
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/postingComments/" + postingCommentId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedComment),
                            MediaType.APPLICATION_JSON
                    ));


            // When
            PostingCommentDto result = sut.getPostingComment(postingCommentId);

            // Then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedComment.id())
                    .hasFieldOrPropertyWithValue("content", expectedComment.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.userAccount().nickname());
            server.verify();
        }

        @DisplayName("Retrieval comment delet API with commentId, delete comment.")
        @Test
        void givenCommentId_whenCallingDeleteCommentApi_thenDeletesComment() throws Exception {
            // Given
            Long postingCommentId = 1L;
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/postingComments/" + postingCommentId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            // When
            sut.deletePostingComment(postingCommentId);

            // Then
            server.verify();
        }
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
                Set.of(RoleType.ADMIN),
                "eunah-test@email.com",
                "eunah-test",
                "test memo"
        );
    }
}