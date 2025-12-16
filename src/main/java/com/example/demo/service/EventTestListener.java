package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Async
public class EventTestListener{
    @EventListener
    public void service(CustomEvent event){
        System.out.println("@EventListener: EventTestListener.service()");
        System.out.println("event.getMsg = " + event.getMsg());
    }
}
