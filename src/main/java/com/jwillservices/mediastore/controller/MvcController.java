package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MvcController {
    private final TagService tagService;

    public MvcController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/welcome")
    public String welcomePage(Model model, HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/";
        }
        model.addAttribute("email", client.getEmail());
        List<Tag> tags = tagService.getTagsByClientId(client.getId());
        model.addAttribute("tags", tags);
        return "welcome";
    }

    @GetMapping("/reels")
    public String reelsPage(HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/";
        }
        return "reels";
    }
}
