package com.example.demo.web.repository;


import com.example.demo.web.domain.Contents;

import java.util.List;

public interface PostRepositoryCustom {
    List<Contents> searchByTitle(String keyword);
}
