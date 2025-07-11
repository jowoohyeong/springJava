package com.example.demo.controller;

import com.example.demo.domain.Notice;
import com.example.demo.service.NoticeService;
import com.example.demo.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    @GetMapping("")

    public String post(Model model){
        model.addAttribute("postList", postService.findList());

        return "post";
    }
}
