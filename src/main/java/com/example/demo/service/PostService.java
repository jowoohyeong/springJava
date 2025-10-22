package com.example.demo.service;

import com.example.demo.domain.Contents;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Contents> findList(){
//        return null;
        return postRepository.findAll();
    }

    public Contents findOne(Long seq){
        return postRepository.findOne(seq);
    }

    @Transactional
    public Contents save(Contents contents) {
        return postRepository.save(contents);
    }

    public void remove(Long seq){
        postRepository.remove(seq);
    }
}
