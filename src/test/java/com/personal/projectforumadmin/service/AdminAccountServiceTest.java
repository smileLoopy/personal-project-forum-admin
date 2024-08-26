package com.personal.projectforumadmin.service;

import com.personal.projectforumadmin.domain.AdminAccount;
import com.personal.projectforumadmin.domain.constant.RoleType;
import com.personal.projectforumadmin.dto.AdminAccountDto;
import com.personal.projectforumadmin.repository.AdminAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("Business Logic - Admin User")
@ExtendWith(MockitoExtension.class)
class AdminAccountServiceTest {

    @InjectMocks private AdminAccountService sut;

    @Mock private AdminAccountRepository adminAccountRepository;

    @DisplayName("Retrieval existing userId, return user data in Optional.")
    @Test
    void givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {
        // Given
        String username = "eunah";
        given(adminAccountRepository.findById(username)).willReturn(Optional.of(createAdminAccount(username)));

        // When
        Optional<AdminAccountDto> result = sut.searchUser(username);

        // Then
        assertThat(result).isPresent();
        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("Retrieval non existing userId, return empty optional.")
    @Test
    void givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
        // Given
        String username = "wrong-user";
        given(adminAccountRepository.findById(username)).willReturn(Optional.empty());

        // When
        Optional<AdminAccountDto> result = sut.searchUser(username);

        // Then
        assertThat(result).isEmpty();
        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("Inser user account information, register by saving the new user account information and return the user account data.")
    @Test
    void givenUserParams_whenSaving_thenSavesAdminAccount() {
        // Given
        AdminAccount adminAccount = createSigningUpAdminAccount("eunah", Set.of(RoleType.USER));
        given(adminAccountRepository.save(adminAccount)).willReturn(adminAccount);

        // When
        AdminAccountDto result = sut.saveUser(
                adminAccount.getUserId(),
                adminAccount.getUserPassword(),
                adminAccount.getRoleTypes(),
                adminAccount.getEmail(),
                adminAccount.getNickname(),
                adminAccount.getMemo()
        );

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("userId", adminAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword", adminAccount.getUserPassword())
                .hasFieldOrPropertyWithValue("roleTypes", adminAccount.getRoleTypes())
                .hasFieldOrPropertyWithValue("email", adminAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", adminAccount.getNickname())
                .hasFieldOrPropertyWithValue("memo", adminAccount.getMemo())
                .hasFieldOrPropertyWithValue("createdBy", adminAccount.getUserId())
                .hasFieldOrPropertyWithValue("modifiedBy", adminAccount.getUserId());
        then(adminAccountRepository).should().save(adminAccount);
    }

    @DisplayName("Get all users")
    @Test
    void givenNothing_whenSelectingAdminAccounts_thenReturnsAllAdminAccounts() {
        // given
        given(adminAccountRepository.findAll()).willReturn(List.of());

        // when
        List<AdminAccountDto> result  = sut.users();

        // then
        assertThat(result).hasSize(0);
        then(adminAccountRepository).should().findAll();
    }

    @DisplayName("Insert userId, delete user account.")
    @Test
    void givenUserId_whenDeleting_thenDeletesAdminAccount() {
        // Given
        String userId = "uno";
        willDoNothing().given(adminAccountRepository).deleteById(userId);

        // When
        sut.deleteUser(userId);

        // Then
        then(adminAccountRepository).should().deleteById(userId);
    }

    private AdminAccount createAdminAccount(String username) {
        return createAdminAccount(username, Set.of(RoleType.USER), null);
    }

    private AdminAccount createSigningUpAdminAccount(String username, Set<RoleType> roleTypes) {
        return createAdminAccount(username, roleTypes, username);
    }

    private AdminAccount createAdminAccount(String username, Set<RoleType> roleTypes, String createdBy) {
        return AdminAccount.of(
                username,
                "password",
                roleTypes,
                "e@mail.com",
                "nickname",
                "memo",
                createdBy
        );
    }

}