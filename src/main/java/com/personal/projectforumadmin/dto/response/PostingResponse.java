package com.personal.projectforumadmin.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.projectforumadmin.dto.PostingDto;
import com.personal.projectforumadmin.dto.UserAccountDto;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostingResponse(
        Long id,
        UserAccountDto userAccount,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static PostingResponse of(Long id, UserAccountDto userAccount, String title, String content, LocalDateTime createdAt) {
        return new PostingResponse(id, userAccount, title, content, createdAt);
    }

    public static PostingResponse withContent(PostingDto dto) {
        return PostingResponse.of(dto.id(), dto.userAccount(), dto.title(), dto.content(), dto.createdAt());
    }

    public static PostingResponse withoutContent(PostingDto dto) {
        return PostingResponse.of(dto.id(), dto.userAccount(), dto.title(), null, dto.createdAt());
    }
}
