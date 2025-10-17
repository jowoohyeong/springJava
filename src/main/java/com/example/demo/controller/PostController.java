package com.example.demo.controller;

import com.example.demo.domain.Board;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String post(Model model){
        model.addAttribute("postList", postService.findList());

        return "post/post";
    }
    @GetMapping("/add")
    public String form(Model model){
        model.addAttribute("board", Board.class);
        return "post/form";
    }
    @PostMapping("/add")
    public void add(Model model, Board board){

    }
}
