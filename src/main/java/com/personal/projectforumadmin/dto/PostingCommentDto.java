package com.personal.projectforumadmin.dto;

import java.time.LocalDateTime;

public record PostingCommentDto(
        Long id,
        Long postingId,
        UserAccountDto userAccount,
        Long parentCommentId,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static PostingCommentDto of(Long id, Long postingId, UserAccountDto userAccount, Long parentCommentId, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new PostingCommentDto(id, postingId, userAccount, parentCommentId, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}
