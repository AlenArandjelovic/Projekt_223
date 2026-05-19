package ch.wiss.m223.controller;


import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
//@CrossOrigin(origins = "http://localhost")
public class PublicController {

    List<String> publicitems = List.of("a","beta","gamma");
    
    @GetMapping("/")
    public List<String> index(Model model) {
        model.addAttribute("message", "Welcome to M223 Public Area");
        return publicitems;
    }
    
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

}
