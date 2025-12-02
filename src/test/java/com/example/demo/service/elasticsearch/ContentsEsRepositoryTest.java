package com.example.demo.service.elasticsearch;

import com.example.demo.domain.document.ContentsDocument;
import com.example.demo.domain.entity.Contents;
import com.example.demo.repository.jpa.ContentsRepository;
import com.example.demo.service.ContentsMapper;
import com.example.demo.service.ContentsEsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ContentsEsRepositoryTest {
    @Autowired
    ContentsEsService contentsEsService;
    @Autowired ContentsRepository contentsRepository;
    @BeforeEach
    @DisplayName("데이터 삽입")
    void save() {
        Contents contents1 = new Contents("Air 정기 프로세스", "writer", "contents");
        Contents contents2 = new Contents("외근 미팅 및 휴가일정", "writer2", "contents2");
        Contents contents3 = new Contents("세스런 기능 개선 필요 항목", "writer3", "contents3");
        contentsRepository.save(contents1);
        contentsRepository.save(contents2);
        contentsRepository.save(contents3);
        contentsEsService.save(ContentsMapper.toDocument(contents1));
        contentsEsService.save(ContentsMapper.toDocument(contents2));
        contentsEsService.save(ContentsMapper.toDocument(contents3));
    }
    @Test
    void search() {
        Iterable<Contents> all = contentsRepository.findAll();
        System.out.println("=========================================");
        for (Contents contents : all) {
            System.out.println("contents = " + contents);
        }
        System.out.println("=========================================");

        List<ContentsDocument> findList = contentsEsService.searchContents("Air");
        for (ContentsDocument contentsDocument : findList) {
            System.out.println("contentsDocument = " + contentsDocument);
        }
        System.out.println("=========================================");
        List<ContentsDocument> findList2 = contentsEsService.searchContents("\\에어 프로세스\\");
        for (ContentsDocument contentsDocument : findList2) {
            System.out.println("contentsDocument = " + contentsDocument);
        }
    }

}