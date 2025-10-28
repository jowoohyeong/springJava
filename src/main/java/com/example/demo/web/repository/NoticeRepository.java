package com.example.demo.web.repository;

import com.example.demo.web.domain.NoticeEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {
    private final EntityManager em;

    public List<NoticeEntity> findAll(){
        return em.createQuery("select i from NoticeEntity i", NoticeEntity.class)
                .getResultList();
    }
}
