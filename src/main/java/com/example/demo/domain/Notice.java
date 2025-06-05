package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="notice")
@Getter
@Setter
@ToString
public class Notice {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdDate;
}
