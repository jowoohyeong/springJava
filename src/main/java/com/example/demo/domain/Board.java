package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "board")       // 테이블 이름 지정
@Getter
@Setter
public class Board {
    @Id @GeneratedValue
    private Long seq;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "writer", length = 100)
    private String writer;

    @Column(name = "created_Date", length = 100)
    private String createdDate;

    @Size(max = 100)
    @Column(name = "content", length = 100)
    private String content;

    @Size(max = 100)
    @Column(name = "reid", length = 100)
    private String reid;

    @Size(max = 100)
    @Column(name = "relev", length = 100)
    private String relev;

    @Size(max = 100)
    @Column(name = "reord", length = 100)
    private String reord;

}
