package com.example.demo.lol.controller;

import com.example.demo.lol.application.LolMetaService;
import com.example.demo.lol.dto.LolMetaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LolMetaPageController {

    private final LolMetaService lolMetaService;

    @GetMapping({"/tft", "/customTFT"})
    public String teamfightList(Model model) {
        LolMetaResponse response = lolMetaService.fetchMetaDecksForPage();
        model.addAttribute("metaList", response.decks());
        return "lol/teamfight-list";
    }

    @GetMapping("/tfttest")
    public String test() {
        return "/test";
    }
}
