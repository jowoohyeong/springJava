package com.example.demo.web.controller;

import com.example.demo.domain.entity.Contents;
import com.example.demo.web.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Validated Contents contents){
        Contents saved = postService.save(contents);

        return ResponseEntity
                .status(CREATED)
                .body(saved.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> edit(@PathVariable Long id, @RequestBody @Validated Contents contents){
        postService.update(id, contents);
        return ResponseEntity.noContent().build(); // 204
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        postService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
