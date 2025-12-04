package com.example.demo.web.controller;

import com.example.demo.domain.document.ContentsDocument;
import com.example.demo.service.ContentsEsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContentsController {
    private final ContentsEsService contentsEsService;
    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }
    @PostMapping("/ping")
    public String pingpost() {
        return "ok";
    }
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam String title,
                                       @RequestParam String seq) {
        log.info("{}", getClass().getName());
        System.out.println("title = " + title);
        System.out.println("seq = " + seq);
        ContentsDocument doc = ContentsDocument.builder()
                .seq(seq)
                .title(title)
                .build();
        contentsEsService.save(doc);

        return new ResponseEntity<>("", HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity search(@RequestParam String keyword) {

        List<ContentsDocument> contentsDocuments = contentsEsService.searchContents(keyword);
        return new ResponseEntity<>(contentsDocuments, HttpStatus.OK);
    }
}
