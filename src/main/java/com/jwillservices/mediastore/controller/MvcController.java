package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.entity.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MvcController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/welcome-page.html")
    public String welcomePage(Model model, HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/";
        }
        model.addAttribute("email", client.getEmail());
        return "welcome-page";
    }
}
