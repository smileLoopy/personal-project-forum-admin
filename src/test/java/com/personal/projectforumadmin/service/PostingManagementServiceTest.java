package com.personal.projectforumadmin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.projectforumadmin.dto.PostingDto;
import com.personal.projectforumadmin.dto.UserAccountDto;
import com.personal.projectforumadmin.dto.properties.ProjectProperties;
import com.personal.projectforumadmin.dto.response.PostingClientResponse;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("Business Logic - Posting Management")
class PostingManagementServiceTest {

    @Disabled("Only for checking real API response result, thus disabled in normal test")
    @DisplayName("Real API test")
    @SpringBootTest
    @Nested
    class RealAPITest {

        private final  PostingManagementService sut;

        @Autowired
        public RealAPITest(PostingManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("Retrieve posting API, return posting")
        @Test
        void givenNothing_whenCallingPostingApi_thenReturnsPostingList() {
            // Given

            // When
            List<PostingDto> result = sut.getPostings();

            // Then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();

        }

    }

    @DisplayName("API mocking test")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(PostingManagementService.class)
    @Nested
    class RestTemplateTest {

        private final  PostingManagementService sut;

        private final ProjectProperties projectProperties;

        private final MockRestServiceServer server;

        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(
                PostingManagementService sut, ProjectProperties projectProperties, MockRestServiceServer server, ObjectMapper mapper
        ) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("Retrieve posting list API, return postings")
        @Test
        void givenNothing_whenCallingPostingsApi_thenReturnPostingList() throws Exception {
            // Given
            PostingDto expectedPosting = createPostingDto("title", "content");;
            PostingClientResponse expectedResponse = PostingClientResponse.of(List.of(expectedPosting));
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/postings?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));
            // When
            List<PostingDto> result = sut.getPostings();

            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedPosting.id())
                    .hasFieldOrPropertyWithValue("title", expectedPosting.title())
                    .hasFieldOrPropertyWithValue("content", expectedPosting.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedPosting.userAccount().nickname());
            server.verify();
        }

        @DisplayName("Retrieve posting API with postingId, return posting.")
        @Test
        void givenPostingId_whenCallingPostingApi_thenReturnsPosting() throws Exception {
            // Given
            Long postingId = 1L;
            PostingDto expectedPosting = createPostingDto("title", "content");
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/postings/" + postingId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedPosting),
                            MediaType.APPLICATION_JSON
                    ));

            // When
            PostingDto result = sut.getPosting(postingId);

            // Then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedPosting.id())
                    .hasFieldOrPropertyWithValue("title", expectedPosting.title())
                    .hasFieldOrPropertyWithValue("content", expectedPosting.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedPosting.userAccount().nickname());
            server.verify();
        }

        @DisplayName("Retrieve posting delete API with postingId, delete posting.")
        @Test
        void givenPostingId_whenCallingDeletePostingApi_thenDeletesPosting() throws Exception {
            // Given
            Long postingId = 1L;
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/postings/" + postingId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            // When
            sut.deletePosting(postingId);

            // Then
            server.verify();
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

}