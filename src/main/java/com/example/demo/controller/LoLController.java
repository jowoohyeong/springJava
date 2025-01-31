package com.example.demo.controller;

import com.example.demo.dto.MetaDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

//        System.out.println(elem);
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
        String output = data+desc;
        return output;
    }

    public Elements dataCrawing(){

        return null;
    }

    @GetMapping("/customTFT")
    public String customTFT(Model model) throws Exception{

        String URL = "https://lolchess.gg/meta";
        Document doc = Jsoup.connect(URL).get();
        // css-1iudmso emls75t0 각 리스트마다 하나씩 체크하는 방법으로 바꿔야댐!
        Elements dataList = doc.select("div.css-1iudmso.emls75t0");


        List<MetaDTO> metaList = new ArrayList<>();
        for(Element data : dataList){
            MetaDTO dto = new MetaDTO();
            // meta 이름 가져오기
            Elements name = data.select("div.css-35tzvc.emls75t4");

            // meta 시너지 가져오기
            Elements image = data.select("ul.Traits.css-1atxqbp.esadftz0");

            dto.setName(name.text().toString());
            dto.setImageDiv(image.toString());

            metaList.add(dto);
        }
        Elements image = doc.select("div.Trait.css-7ea5v7.e4d3e1w0");


        // 메타내 소속 챔피언 class="Champions css-18e12to e1mdo1l0"
        // 각 챔피언 class = "Champion css-10ce5p7 e9927jh0"

/*
        for(Element element : elem){
            MetaDTO dto = new MetaDTO();
            // 신규덱 찾기 class = "tag updated"
            //            Elements spans = div.select("span");
            dto.setName(element.text().toString());
            metaList.add(dto);
        }
*/
        model.addAttribute("metaList", metaList);

        return "teamfight/list";
    }

    @GetMapping("tfttest")
    public String test(){
        return "/test";
    }
}
