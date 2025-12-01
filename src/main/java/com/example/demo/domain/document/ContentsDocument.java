package com.example.demo.domain.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "contents")
public class ContentsDocument {
    @Id
    private Long seq;

    @Field(type = FieldType.Text,
            analyzer = "ngram_analyzer",       // 부분 문자열 검색용 분석기
            searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Keyword)
    private String writer;

    @Field(type = FieldType.Date)
    private LocalDateTime regDate;

    @Field(type = FieldType.Text)
    private String content;
}

