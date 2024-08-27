package com.personal.projectforumadmin.service;

import com.personal.projectforumadmin.dto.PostingDto;
import com.personal.projectforumadmin.dto.properties.ProjectProperties;
import com.personal.projectforumadmin.dto.response.PostingClientResponse;
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
public class PostingManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;

    public List<PostingDto> getPostings() {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/postings")
                .queryParam("size", 10000) // TODO: A way to pass a size large enough to retrieve the entire post. It's incomplete.
                .build()
                .toUri();
        PostingClientResponse response = restTemplate.getForObject(uri, PostingClientResponse.class);

        return Optional.ofNullable(response)
                .orElseGet(PostingClientResponse::empty).postings();
    }

    public PostingDto getPosting(Long postingId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/postings/" + postingId)
                .build()
                .toUri();
        PostingDto response = restTemplate.getForObject(uri, PostingDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("There is no posting - postingId: " + postingId));
    }

    public void deletePosting(Long postingId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/postings/" + postingId)
                .build()
                .toUri();
        restTemplate.delete(uri);
    }
}