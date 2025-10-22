package com.example.demo.web.service;

import com.example.demo.web.domain.Contents;
import com.example.demo.web.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@CacheConfig(cacheNames = "post")
public class PostService {

    private final PostRepository postRepository;

    @Cacheable(key = "'all'")
    public List<Contents> findList(){
        return postRepository.findAll();
    }

    @Cacheable(key = "#seq", unless = "#result == null")
    public Contents findOne(Long seq){
        return postRepository.findById(seq).orElse(null);
    }

    @CacheEvict(key = "'all'")
    @CachePut(key = "#contents.seq")
    @Transactional
    public Contents save(Contents contents) {
        return postRepository.save(contents);
    }

    @CacheEvict(key = "'all'")
    @Transactional
    public void deleteBySeq(Long seq){
        postRepository.deleteById(seq);
    }
}
