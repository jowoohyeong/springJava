package com.example.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
@AllArgsConstructor
public class DemoController {

    private final RedisTemplate<String, Object> redisTemplate;
    @GetMapping("/redis-test")
    @ResponseBody
    public String redisTest(@RequestParam(required = false, defaultValue = "1") int i){
        System.out.println("i = " + i);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        System.out.println("valueOperations = " + valueOperations);
        String k = "key" + i;
        valueOperations.set(k, "3");

        String str = "";
        Set<String> keys = redisTemplate.keys("*");

        for(String key : keys){
            str += "<div> key : " +  key + "<t>, value : " + valueOperations.get(key) + "</div>";
        }

        return str;
    }


}
