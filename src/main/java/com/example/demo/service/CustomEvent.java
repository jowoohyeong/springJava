package com.example.demo.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CustomEvent extends ApplicationEvent {
    private String msg;

    public CustomEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

}
