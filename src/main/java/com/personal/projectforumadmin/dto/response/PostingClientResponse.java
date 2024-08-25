package com.personal.projectforumadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.personal.projectforumadmin.dto.PostingDto;

import java.util.List;


public record PostingClientResponse(
        @JsonProperty("_embedded") Embedded embedded,
        @JsonProperty("page") Page page
) {

    public static PostingClientResponse empty() {
        return new PostingClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static PostingClientResponse of(List<PostingDto> postings) {
        return new PostingClientResponse(
                new Embedded(postings),
                new Page(postings.size(), postings.size(), 1, 0)
        );
    }

    public List<PostingDto> postings() { return this.embedded().postings(); }

    public record Embedded(List<PostingDto> postings) {}

    public record Page(
            int size,
            long totalElements,
            int totalPages,
            int number
    ) {}

}