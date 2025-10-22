package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contents")       // 테이블 이름 지정
@Data
public class Contents {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotBlank
    @Column(name = "title", length = 100)
    private String title;

    @NotBlank
    @Column(name = "writer", length = 100)
    private String writer;

    @CreationTimestamp
    @Column(name = "reg_date")
    private LocalDateTime regDate;

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
