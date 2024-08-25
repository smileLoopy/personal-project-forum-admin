package com.personal.projectforumadmin.service;

import com.personal.projectforumadmin.dto.PostingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostingManagementService {

    public List<PostingDto> getPostings() {
        return List.of();
    }

    public PostingDto getPosting(Long postingId) {
        return null;
    }

    public void deletePosting(Long postingId) {
    }
}
