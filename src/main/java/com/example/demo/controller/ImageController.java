package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImageController {

    @GetMapping("/image")
    public String image(Model model){
        String path = "/images/";
        String filename = "맹구4.png";
        model.addAttribute("imagePath", path+filename);

        return "image";

    }
}
