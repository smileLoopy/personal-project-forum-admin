package com.personal.projectforumadmin.controller;


import com.personal.projectforumadmin.dto.response.PostingResponse;
import com.personal.projectforumadmin.service.PostingManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/management/postings")
@Controller
public class PostingManagementController {

    private final PostingManagementService postingManagementService;

    @GetMapping
    public String postings(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            Model model
    ) {
        String requestURI = request.getRequestURI();
        model.addAttribute("isPostings",
                "/management/postings".equals(requestURI)
        );
        model.addAttribute("postings", postingManagementService.getPostings()
                .stream()
                .map(PostingResponse::withoutContent)
                .toList()
        );
        return "management/postings";
    }

    @ResponseBody
    @GetMapping("/{postingId}")
    public PostingResponse posting(@PathVariable Long postingId) {
        return PostingResponse.withContent(postingManagementService.getPosting(postingId));
    }

    @PostMapping("/{postingId}")
    public String deletePosting(@PathVariable Long postingId) {
        postingManagementService.deletePosting(postingId);

        return "redirect:/management/postings";
    }
}
