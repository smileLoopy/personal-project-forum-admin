package com.personal.projectforumadmin.service;

import com.personal.projectforumadmin.dto.PostingCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostingCommentManagementService {

    public List<PostingCommentDto> getPostingComments() {
        return List.of();
    }

    public PostingCommentDto getPostingComment(Long postingCommentId) {
        return null;
    }

    public void deletePostingComment(Long postingCommentId) {

    }
}
