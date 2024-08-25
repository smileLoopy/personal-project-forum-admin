package com.personal.projectforumadmin.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record PostingDto(
        Long id,
        UserAccountDto userAccount,
        String title,
        String content,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static PostingDto of(Long id, UserAccountDto userAccount, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new PostingDto(id, userAccount, title, content, hashtags, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}
