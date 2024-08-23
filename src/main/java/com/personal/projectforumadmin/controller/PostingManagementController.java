package com.personal.projectforumadmin.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/management/postings")
@Controller
public class PostingManagementController {

    @GetMapping
    public String postings(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            Model model
    ) {
        String requestURI = request.getRequestURI();
        model.addAttribute("isPostings", "/management/postings".equals(requestURI));
        return "management/postings";
    }
}
