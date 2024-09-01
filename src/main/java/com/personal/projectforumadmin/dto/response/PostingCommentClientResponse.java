package com.personal.projectforumadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.personal.projectforumadmin.dto.PostingCommentDto;

import java.util.List;

public record PostingCommentClientResponse(
        @JsonProperty("_embedded") Embedded embedded,
        @JsonProperty("page") Page page
) {

    public static PostingCommentClientResponse empty() {
        return new PostingCommentClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static PostingCommentClientResponse of(List<PostingCommentDto> postingComments) {
        return new PostingCommentClientResponse(
                new Embedded(postingComments),
                new Page(postingComments.size(), postingComments.size(), 1, 0)
        );
    }

    public List<PostingCommentDto> postingComments() {
        return this.embedded().postingComments();
    }

    public record Embedded(List<PostingCommentDto> postingComments) {}

    public record Page(
            int size,
            long totalElements,
            int totalPages,
            int number
    ) {}
}