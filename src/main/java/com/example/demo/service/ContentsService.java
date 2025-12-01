package com.example.demo.service;

import com.example.demo.domain.document.ContentsDocument;
import com.example.demo.repository.elasticsearch.ContentsEsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentsService {
    private final ContentsEsRepository contentsEsRepository;

    public ContentsDocument save(ContentsDocument doc){
        return contentsEsRepository.save(doc);
    }
    public List<ContentsDocument> searchTitle(String keyword) {
        return contentsEsRepository.findByTitleContaining(keyword);
    }
}
