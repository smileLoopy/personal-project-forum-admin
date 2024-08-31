package com.personal.projectforumadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.projectforumadmin.dto.PostingCommentDto;
import com.personal.projectforumadmin.dto.UserAccountDto;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostingCommentResponse(
        Long id,
        UserAccountDto userAccount,
        String content,
        LocalDateTime createdAt
) {

    public static PostingCommentResponse of(Long id, UserAccountDto userAccount, String content, LocalDateTime createdAt) {
        return new PostingCommentResponse(id, userAccount, content, createdAt);
    }

    public static PostingCommentResponse of(PostingCommentDto dto) {
        return PostingCommentResponse.of(dto.id(), dto.userAccount(), dto.content(), dto.createdAt());
    }
}
