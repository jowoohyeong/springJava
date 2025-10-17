package com.example.demo.repository;

import com.example.demo.domain.Notice;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {
    private final EntityManager em;

    public List<Notice> findAll(){
        return em.createQuery("select i from Notice i", Notice.class)
                .getResultList();
    }
}
