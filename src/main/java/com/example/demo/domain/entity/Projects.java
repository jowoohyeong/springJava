package com.example.demo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "client"})
public class Projects {
    @Id @GeneratedValue
    @Column(name = "project_id")
    private Long id;
    private String name;
    private String client;

    public Projects(String name, String client) {
        this.name = name;
        this.client = client;
    }

    private String team;
}
