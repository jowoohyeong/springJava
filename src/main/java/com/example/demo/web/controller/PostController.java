package com.example.demo.web.controller;

import com.example.demo.domain.entity.Contents;
import com.example.demo.web.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "게시글 목록 조회",  description = "게시글 전체 목록을 조회합니다.")
    public String post(Model model){
        model.addAttribute("postList", postService.findList());
        return "post/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findOne(id));
        return "post/view";
    }
    @GetMapping("/new")
    public String addForm(Model model){
        model.addAttribute("contents", new Contents());
        return "post/add";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findOne(id));
        return "post/edit";
    }


}
