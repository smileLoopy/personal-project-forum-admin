package com.personal.projectforumadmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/management/tip")
@Controller
public class TipController {

    @GetMapping
    public String tip(){
        return "management/tip";
    }
}
