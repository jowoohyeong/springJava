package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoLController {

    @GetMapping("/tft")
    @ResponseBody
    public String tft() throws Exception{
        String URL = "https://lolchess.gg/meta";
        Document doc = Jsoup.connect(URL).get();

        //<div class="css-s9pipd e2kj5ne0">
        Elements elem = doc.select("div.css-s9pipd.e2kj5ne0");

        System.out.println(elem);
        String data = elem.toString();

        // 덱 이름 추출.
        Elements divs = doc.select("div.css-1xsl2fm.emls75t4");
        List<String> metaName = divs.stream().map(Element::text).toList();

        Elements metaDivs = doc.select("div.css-1vo3wqf.emls75t3");
        List<String> metaMember = metaDivs.stream().map(Element::text).toList();

        // 아이콘 설명
        Elements description = doc.select("script#__NEXT_DATA__");
        String desc = description.toString();
        System.out.println("================================================");
//        __NEXT_DATA__
        //제목만
        for (int i =0; i < metaName.size(); i++) {
            String name = metaName.get(i);
            String[] members = metaMember.get(i).split(" ");
            String member = "";
            System.out.println("< " + name + " > 총:" + members[members.length-4]);

            for(int j =0;j<members.length-4;j++){
                member += members[j];
            }
            System.out.println(member);

        }

        return data+desc;
    }

    @GetMapping("tfttest")
    public String test(){
        return "/test";
    }
}
