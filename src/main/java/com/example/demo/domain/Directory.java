package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Directory {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    public Directory(String name) {
        this.name = name;
    }

    public Directory() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
