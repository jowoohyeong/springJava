package com.example.demo.web.repository;

import com.example.demo.web.domain.Contents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Contents, Long> {

}
