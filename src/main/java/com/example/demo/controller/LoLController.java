package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoLController {

    @GetMapping("/tft")
    @ResponseBody
    public String tft() throws Exception{
        int cnt = 0;
        String URL = "https://lolchess.gg/meta";
        Document doc = Jsoup.connect(URL).get();

        //<div class="css-s9pipd e2kj5ne0">
        Elements elem = doc.select("div.css-s9pipd.e2kj5ne0");

        System.out.println(elem);
        String data = elem.toString();
        System.out.println("================================================");

        for(Element e: elem.select("dt")) {
//            if (e.className().equals("photo")) {
//                continue;
//            }
            System.out.println(e.text());

            data += e.text();
            cnt++;
        }

        System.out.println("================================================");
        //제목만
        for(Element e: elem.select("Strong")) {
            //System.out.println(e.text());
            cnt++;
        }
        System.out.println(cnt);

        return data;
    }
}
