package com.example.demo.web.controller;

import com.example.demo.domain.document.ContentsDocument;
import com.example.demo.service.ContentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContentsController {
    private final ContentsService contentsService;
    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }
    @PostMapping("/save")
    public ContentsDocument save(@RequestParam String title, @RequestParam Long seq) {
        System.out.println("title = " + title);
        System.out.println("seq = " + seq);
        ContentsDocument doc = ContentsDocument.builder()
                .seq(seq)
                .title(title)
                .build();
        return contentsService.save(doc);
    }
    @GetMapping("/search")
    public List<ContentsDocument> search(@RequestParam String keyword) {
        return contentsService.searchTitle(keyword);
    }
}
