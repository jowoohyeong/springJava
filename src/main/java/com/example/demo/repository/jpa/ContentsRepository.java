package com.example.demo.repository.jpa;

import com.example.demo.domain.entity.Contents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
}
