package com.personal.projectforumadmin.repository;

import com.personal.projectforumadmin.domain.UserAccount;
import com.personal.projectforumadmin.domain.constant.RoleType;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA Connection Test")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final UserAccountRepository userAccountRepository;;

    public JpaRepositoryTest(@Autowired UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("User Information Select Test")
    @Test
    void givenUserAccounts_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<UserAccount> userAccounts = userAccountRepository.findAll();

        // Then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("User Information Insert Test")
    @Test
    void givenUserAccounts_whenInserting_thenWorksFine() {
        // Given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = UserAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);

        // When
        userAccountRepository.save(userAccount);

        // Then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("User Information Update Test")
    @Test
    void givenUserAccountAndRoleType_whenUpdating_thenWorksFine() {
        // Given
        UserAccount userAccount = userAccountRepository.getReferenceById("eunah");
        userAccount.addRoleType(RoleType.DEVELOPER);
        userAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        userAccount.removeRoleType(RoleType.ADMIN);

        // When
        UserAccount updatedAccount = userAccountRepository.saveAndFlush(userAccount);;

        // Then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "eunah")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.USER, RoleType.DEVELOPER));
    }

    @DisplayName("User Information Delete Test")
    @Test
    void givenUserAccount_whenDeleting_thenWorksFine() {
        // Given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = userAccountRepository.getReferenceById("eunah");

        // When
        userAccountRepository.delete(userAccount);;

        // Then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount - 1);
    }

    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return  () -> Optional.of("eunah");
        }
    }

}