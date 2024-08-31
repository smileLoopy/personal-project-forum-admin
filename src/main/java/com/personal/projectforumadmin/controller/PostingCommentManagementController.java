package com.personal.projectforumadmin.controller;


import com.personal.projectforumadmin.dto.response.PostingCommentResponse;
import com.personal.projectforumadmin.service.PostingCommentManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/management/posting-comments")
@Controller
public class PostingCommentManagementController {

    private final PostingCommentManagementService postingCommentManagementService;;

    @GetMapping
    public String postingComments(
            HttpServletRequest request,
            Model model
    ) {
        String requestURI = request.getRequestURI();
        model.addAttribute("isComments", "/management/posting-comments".equals(requestURI));
        model.addAttribute(
                "comments",
                postingCommentManagementService.getPostingComments().stream().map(PostingCommentResponse::of).toList()
        );
        return "management/posting-comments";
    }

    @ResponseBody
    @GetMapping("/{postingCommentId}")
    public PostingCommentResponse postingComment(@PathVariable Long postingCommentId) {
        return PostingCommentResponse.of(postingCommentManagementService.getPostingComment((postingCommentId)));
    }

    @PostMapping("/{postingCommentId}")
    public String deletePostingComment(@PathVariable Long postingCommentId) {
        postingCommentManagementService.deletePostingComment(postingCommentId);

        return "redirect:/management/posting-comments";
    }
}
