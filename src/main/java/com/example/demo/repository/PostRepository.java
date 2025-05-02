package com.example.demo.repository;

import com.example.demo.domain.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    public List<Post> findAll(){
        return em.createQuery("select i from Post i", Post.class)
                .getResultList();
    }
}
