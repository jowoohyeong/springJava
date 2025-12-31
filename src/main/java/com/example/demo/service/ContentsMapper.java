package com.example.demo.service;

import com.example.demo.domain.document.ContentsDocument;
import com.example.demo.domain.entity.Contents;

public class ContentsMapper {
    public static ContentsDocument toDocument(Contents entity) {
        return ContentsDocument.builder()
                .id(String.valueOf(entity.getId()))
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .content(entity.getContent())
                .build();
    }
}
