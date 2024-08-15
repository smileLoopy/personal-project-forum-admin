package com.personal.projectforumadmin.dto.security;

import com.personal.projectforumadmin.domain.constant.RoleType;
import com.personal.projectforumadmin.dto.UserAccountDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record ForumAdminPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User {

    public static ForumAdminPrincipal of(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return ForumAdminPrincipal.of(username, password, roleTypes, email, nickname, memo, Map.of());
    }

    public static ForumAdminPrincipal of(String username, String password, Set<RoleType> roleTypes,  String email, String nickname, String memo, Map<String, Object> oAuth2Attributes) {
        return new ForumAdminPrincipal(
                username,
                password,
                roleTypes.stream()
                    .map(RoleType::getRoleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toUnmodifiableSet()),
                email,
                nickname,
                memo,
                oAuth2Attributes
        );
    }

    public static ForumAdminPrincipal from(UserAccountDto dto) {
        return ForumAdminPrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.roleTypes(),
                dto.email(),
                dto.nickname(),
                dto.memo()
        );
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                username,
                password,
                authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(RoleType::valueOf)
                    .collect(Collectors.toUnmodifiableSet()),
                email,
                nickname,
                memo
        );
    }

    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    @Override
    public Map<String, Object> getAttributes() { return oAuth2Attributes; }

    @Override
    public String getName() { return username; }

}
