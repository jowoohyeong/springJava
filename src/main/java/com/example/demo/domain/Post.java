package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board")       // 테이블 이름 지정
@Getter
@Setter
public class Post {
    @Id @GeneratedValue
    private Long seq;

    private String title;
//    private String author;
    private String writer;
    private String createdDate;

}
