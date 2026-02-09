package com.jwillservices.mediastore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/welcome-page.html")
    public String welcomePage(Model model) {
        return "welcome-page";
    }
}
