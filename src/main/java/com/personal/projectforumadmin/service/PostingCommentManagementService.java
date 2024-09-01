package com.personal.projectforumadmin.service;

import com.personal.projectforumadmin.dto.PostingCommentDto;
import com.personal.projectforumadmin.dto.properties.ProjectProperties;
import com.personal.projectforumadmin.dto.response.PostingCommentClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostingCommentManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;


    public List<PostingCommentDto> getPostingComments() {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/postingComments")
                .queryParam("size", 10000)
                .build()
                .toUri();
        PostingCommentClientResponse response = restTemplate.getForObject(uri, PostingCommentClientResponse.class);

        return Optional.ofNullable(response).orElseGet(PostingCommentClientResponse::empty).postingComments();
    }

    public PostingCommentDto getPostingComment(Long postingCommentId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/postingComments/" + postingCommentId)
                .queryParam("projection", "withUserAccount")
                .build()
                .toUri();
        PostingCommentDto response = restTemplate.getForObject(uri, PostingCommentDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("There is no comment - postingCommentId: " + postingCommentId));
    }

    public void deletePostingComment(Long postingCommentId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/postingComments/" + postingCommentId)
                .build()
                .toUri();
        restTemplate.delete(uri);

    }
}
