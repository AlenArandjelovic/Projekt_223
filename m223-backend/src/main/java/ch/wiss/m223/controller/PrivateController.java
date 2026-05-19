package ch.wiss.m223.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Welcome to M223 private Area");
        return "index";
    }
    
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

}
