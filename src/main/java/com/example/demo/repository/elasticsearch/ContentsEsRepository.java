package com.example.demo.repository.elasticsearch;

import com.example.demo.domain.document.ContentsDocument;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
@Profile("local")
public interface ContentsEsRepository extends ElasticsearchRepository<ContentsDocument, String> {
    List<ContentsDocument> findByTitle(String searchTitle);

    List<ContentsDocument> findByTitleContaining(String keyword);

}
