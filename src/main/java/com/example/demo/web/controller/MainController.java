package com.example.demo.web.controller;

import com.example.demo.web.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("list", mainService.list());
        return "index";
    }

}
