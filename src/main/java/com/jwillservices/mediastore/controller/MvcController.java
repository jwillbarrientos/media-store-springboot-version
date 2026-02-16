package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.repository.TagRepository;
import com.jwillservices.mediastore.service.ClientService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MvcController {
    private final TagRepository tagRepository;
    private final ClientService clientService;

    public MvcController(TagRepository tagRepository, ClientService clientService) {
        this.tagRepository = tagRepository;
        this.clientService = clientService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/welcome")
    public String welcomePage(Model model, Principal principal) {
        // Obtener cliente para acceder a su ID
        Client client = clientService.findByEmail(principal.getName());
        if (client != null) {
            List<Tag> tags = tagRepository.findTagsByClientId(client.getId());
            model.addAttribute("tags", tags);
        }
        return "welcome";
    }

    @GetMapping("/reels")
    public String reelsPage() {
        return "reels";
    }
}
