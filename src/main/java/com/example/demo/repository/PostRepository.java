package com.example.demo.repository;

import com.example.demo.domain.Contents;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@CacheConfig(cacheNames = "contents")
public class PostRepository {
    private final EntityManager entityManager;

    @Cacheable(key = "'all'")
    public List<Contents> findAll(){
        return entityManager.createQuery("select i from Contents i", Contents.class)
                .getResultList();
    }

    @Cacheable(key = "#seq", unless = "#result == null")
    public Contents findOne(Long seq) {
        return entityManager.find(Contents.class, seq);
    }

    @CachePut(key = "#contents.seq")
    @CacheEvict(key = "'all'")
    public Contents save(Contents contents){
        if (contents.getSeq() == null) {
            entityManager.persist(contents);
            return contents;
        } else {
            return entityManager.merge(contents);
        }
    }

    public void remove(Long seq) {
        entityManager.remove(seq);
    }

}
