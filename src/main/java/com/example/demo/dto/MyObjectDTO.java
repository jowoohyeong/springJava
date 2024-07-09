package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class MyObjectDTO implements Serializable {

    private String name;
    private String description;

    public MyObjectDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
