package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventTestListener{
    @EventListener
    public void service(CustomEvent event){
        System.out.println("EventTestListener.onApplicationEvent");
        System.out.println("event.getMsg = " + event.getMsg());
    }

}
