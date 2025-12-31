package com.example.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name = "title", length = 100)
    private String title;

    @NotBlank
    @Column(name = "writer", length = 100)
    private String writer;

    public Contents() {
    }

    public Contents(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)       //직렬화시 필요
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)   //역직렬화시 필요
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")                //원하는 형태의 LocalDateTime
    @CreationTimestamp
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Size(max = 100)
    @Column(name = "content", length = 100)
    private String content;
}
