package com.example.demo.web.repository;

import com.example.demo.web.domain.Contents;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryLegacy {
    private final EntityManager entityManager;

    public List<Contents> findAll(){
        return entityManager.createQuery("select i from Contents i", Contents.class)
                .getResultList();
    }


    public Contents findOne(Long seq) {
        return entityManager.find(Contents.class, seq);
    }

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
