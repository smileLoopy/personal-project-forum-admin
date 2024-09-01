package com.personal.projectforumadmin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.projectforumadmin.dto.UserAccountDto;
import com.personal.projectforumadmin.dto.properties.ProjectProperties;
import com.personal.projectforumadmin.dto.response.UserAccountClientResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("Business Logic - User Management")
class UserAccountManagementServiceTest {

    @Disabled("Only for checking real API response result, thus disabled in normal test")
    @DisplayName("Real API Retrieval Test")
    @SpringBootTest
    @Nested
    class RealApiTest {

        private final UserAccountManagementService sut;

        @Autowired
        public RealApiTest(UserAccountManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("Retrieval user API, return user lists.")
        @Test
        void givenNothing_whenCallingUserAccountApi_thenReturnsUserAccountList() {
            // Arrange

            // Act
            List<UserAccountDto> result = sut.getUserAccounts();

            // Assert
            System.out.println(result.stream().findFirst()); // The purpose of this test is to check result of API retrieval
            assertThat(result).isNotNull();
        }
    }


    @DisplayName("API mocking test")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(UserAccountManagementService.class)
    @Nested
    class RestTemplateTest {

        private final UserAccountManagementService sut;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(
                UserAccountManagementService sut,
                ProjectProperties projectProperties,
                MockRestServiceServer server,
                ObjectMapper mapper

        ) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("Retrieval user account list API, return user account list.")
        @Test
        void givenNothing_whenCallingUserAccountsApi_thenReturnsUserAccountList() throws Exception {
            // Given
            UserAccountDto expectedUserAccount = createUserAccountDto("eunah", "Eunah");
            UserAccountClientResponse expectedResponse = UserAccountClientResponse.of(List.of(expectedUserAccount));
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/userAccounts?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));


            // When
            List<UserAccountDto> result = sut.getUserAccounts();

            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("userId", expectedUserAccount.userId())
                    .hasFieldOrPropertyWithValue("nickname", expectedUserAccount.nickname());
            server.verify();
        }

        @DisplayName("Retrieval user account API with userId, return user. account")
        @Test
        void givenUserAccountId_whenCallingUserAccountApi_thenReturnsUserAccount() throws Exception {
            // Given
            String userId = "eunah";
            UserAccountDto expectedUserAccount = createUserAccountDto(userId, "Eunah");
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/userAccounts/" + userId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedUserAccount),
                            MediaType.APPLICATION_JSON
                    ));


            // When
            UserAccountDto result = sut.getUserAccount(userId);

            // Then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("userId", expectedUserAccount.userId())
                    .hasFieldOrPropertyWithValue("nickname", expectedUserAccount.nickname());
            server.verify();
        }

        @DisplayName("Retrieval user account delete API with userId, delete user account.")
        @Test
        void givenUserAccountId_whenCallingDeleteUserAccountApi_thenDeletesUserAccount() throws Exception {
            // Given
            String userId = "eunah";
            server
                    .expect(requestTo(projectProperties.forum().url() + "/api/userAccounts/" + userId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            // When
            sut.deleteUserAccount(userId);

            // Then
            server.verify();
        }
    }


    private UserAccountDto createUserAccountDto(String userId, String nickname) {
        return UserAccountDto.of(
                userId,
                "eunah-test@email.com",
                nickname,
                "test memo"
        );
    }

}