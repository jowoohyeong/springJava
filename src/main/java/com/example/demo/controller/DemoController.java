package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class DemoController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
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
    @GetMapping("/webSpeech")
    public String webSpeech(){
        return "webSpeech";
    }
    @GetMapping("/test")
    public ResponseEntity<?> getUsers() {
        System.out.println("test");
        List<Map<String, Object>> collect = new ArrayList<>();
        try {
               List<Map<String, Object>> users = objectMapper.readValue(
                   Files.readAllBytes(Paths.get("/data/", "records.json")),
                   new TypeReference<>() {
               });


            users.stream().sorted(Comparator.comparing(map-> (String)map.get("username"))).collect(Collectors.toList());

            return ResponseEntity.ok("Hello, world!");
        } catch (IOException e) {
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    @RequestMapping("/iframe")
    public String example(){
        return "/iframe";
    }


}
