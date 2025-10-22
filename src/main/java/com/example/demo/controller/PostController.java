package com.example.demo.controller;

import com.example.demo.domain.Contents;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
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

    @GetMapping("/{seq}")
    public String view(Model model, @PathVariable Long seq) {
        model.addAttribute("post", postService.findOne(seq));
        return "post/view";
    }

    @GetMapping("/add")
    public String form(Model model){
        model.addAttribute("contents", new Contents());
        return "post/form";
    }
    @PostMapping("/add")
    public String add(@Validated @ModelAttribute("contents") Contents contents, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());


        if (bindingResult.hasErrors()) {
            // bindingResult 자동으로 model에 추가됨
            log.info("errors = {} ", bindingResult);
            return "post/form";
        }

        Contents contents1 = postService.save(contents);

        redirectAttributes.addAttribute("seq", contents1.getSeq());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/post/{seq}";
    }

    @DeleteMapping("/{seq}")
    public String remove(@PathVariable Long seq){
        postService.remove(seq);

        return "redirect:/post";
    }
}
