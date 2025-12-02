package com.example.demo.service;

import com.example.demo.domain.document.ContentsDocument;
import com.example.demo.repository.elasticsearch.ContentsEsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentsEsService {
    private final ContentsEsRepository contentsEsRepository;

    public void save(ContentsDocument doc){
        contentsEsRepository.save(doc);
    }
    public List<ContentsDocument> searchContents(String keyword) {
        return contentsEsRepository.findByTitle(keyword);
    }
}
