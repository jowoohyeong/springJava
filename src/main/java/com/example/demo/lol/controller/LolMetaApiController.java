package com.example.demo.lol.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.lol.application.LolMetaService;
import com.example.demo.lol.dto.LolMetaResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lol")
@RequiredArgsConstructor
public class LolMetaApiController {

    private final LolMetaService lolMetaService;

    @GetMapping("/meta")
    @Operation(summary = "TFT 메타 덱 크롤링", description = "lolchess.gg/meta 페이지의 임베디드 데이터를 파싱해 메타 덱 목록을 반환합니다.")
    public ResponseEntity<ApiResponse<LolMetaResponse>> fetchMetaDecks() {
        LolMetaResponse response = lolMetaService.fetchMetaDecks();
        return ResponseEntity.ok(new ApiResponse<>(true, "lolchess meta decks fetched", response));
    }
}
