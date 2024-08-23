package com.personal.projectforumadmin.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/management/user-accounts")
@Controller
public class UserAccountManagementController {

    @GetMapping
    public String userAccounts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            Model model
    ) {
        String requestURI = request.getRequestURI();
        model.addAttribute("isUserAccounts", "/management/user-accounts".equals(requestURI));
        return "management/user-accounts";
    }

}
