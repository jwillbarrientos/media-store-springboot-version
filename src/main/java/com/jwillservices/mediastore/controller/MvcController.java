package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.service.TagService;
import com.jwillservices.mediastore.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.expression.Mvc;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MvcController {
    private final VideoService videoService;
    private final TagService tagService;

    public MvcController(VideoService videoService, TagService tagService) {
        this.videoService = videoService;
        this.tagService = tagService;
    }

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
        List<Tag> tags = tagService.getTagsByClientId(client.getId());
        model.addAttribute("tags", tags);
        return "welcome-page";
    }

    @GetMapping("/reels.html")
    public String reelsPage(HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/";
        }
        return "reels";
    }

    @GetMapping("/reels.html")
    public String reelsPage(@RequestParam String tag, Model model, HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/";
        }
        List<Video> videos = videoService.getVideosByTag(client, tag);
        model.addAttribute("videos", videos);
        return "reels";
    }
}
