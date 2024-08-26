package com.personal.projectforumadmin.repository;

import com.personal.projectforumadmin.domain.AdminAccount;
import com.personal.projectforumadmin.domain.constant.RoleType;
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

    private final AdminAccountRepository adminAccountRepository;;

    public JpaRepositoryTest(@Autowired AdminAccountRepository adminAccountRepository) {
        this.adminAccountRepository = adminAccountRepository;
    }

    @DisplayName("User Account Information Select Test")
    @Test
    void givenAdminAccounts_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<AdminAccount> adminAccounts = adminAccountRepository.findAll();

        // Then
        assertThat(adminAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("User Account Information Insert Test")
    @Test
    void givenAdminAccounts_whenInserting_thenWorksFine() {
        // Given
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = AdminAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);

        // When
        adminAccountRepository.save(adminAccount);

        // Then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("User Account Information Update Test")
    @Test
    void givenAdminAccountAndRoleType_whenUpdating_thenWorksFine() {
        // Given
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("eunah");
        adminAccount.addRoleType(RoleType.DEVELOPER);
        adminAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        adminAccount.removeRoleType(RoleType.ADMIN);

        // When
        AdminAccount updatedAccount = adminAccountRepository.saveAndFlush(adminAccount);;

        // Then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "eunah")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.USER, RoleType.DEVELOPER));
    }

    @DisplayName("User Account Information Delete Test")
    @Test
    void givenAdminAccount_whenDeleting_thenWorksFine() {
        // Given
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("eunah");

        // When
        adminAccountRepository.delete(adminAccount);;

        // Then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount - 1);
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