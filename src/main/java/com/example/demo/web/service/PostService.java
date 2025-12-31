package com.example.demo.web.service;

import com.example.demo.domain.entity.Contents;
import com.example.demo.web.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "post")
public class PostService {

    private final PostRepository postRepository;

    @Cacheable(key = "'all'")
    public List<Contents> findList(){
        return postRepository.findAll();
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public Contents findOne(Long id){
        return postRepository.findById(id).orElse(null);
    }

    @CacheEvict(key = "'all'")
    @CachePut(key = "#contents.id")
    @Transactional
    public Contents save(Contents contents) {
        return postRepository.save(contents);
    }
    @CacheEvict(key = "'all'")
    @CachePut(key = "#id")
    @Transactional
    public Contents update(Long id, Contents contents) {
        Contents findPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        findPost.setTitle(contents.getTitle());
//        findPost.setWriter(contents.getWriter());
        findPost.setContent(contents.getContent());

        return findPost;
    }

    @CacheEvict(key = "'all'")
    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }
}
