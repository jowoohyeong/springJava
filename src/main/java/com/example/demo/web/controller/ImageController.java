package com.example.demo.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ImageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    final private String path = "/images/";
    @GetMapping("/image")
    public String image(Model model){
        logger.info("image > 맹구 테스트");

        String filename = "맹구4.png";

        model.addAttribute("imagePath", path+filename);

        return "image/image";
    }
    @GetMapping("image-list")
    public String imageList(Model model){

        File directory = new File("D:/workspace/demo/src/main/resources/static/images/");
        File[] filesList = directory.listFiles();
        // 파일과 디렉터리 출력

        List<String> imageList = new ArrayList<>();

        if (filesList != null) {
            for (File file : filesList) {
                if (file.isFile()) {
                    System.out.println("파일: " + file.getName());
                    imageList.add(file.getName());
                } else if (file.isDirectory()) {
                    System.out.println("디렉터리: " + file.getName());
                }
            }
        } else {
            System.out.println("디렉터리를 찾을 수 없습니다.");
        }
        model.addAttribute("path", path);
        model.addAttribute("imageList", imageList);
        return "image/image-list";
    }
}
