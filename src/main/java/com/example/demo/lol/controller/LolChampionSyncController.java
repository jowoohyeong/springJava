package com.example.demo.lol.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.lol.application.LolChampionSyncService;
import com.example.demo.lol.dto.LolChampionSyncResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lol/champions")
@RequiredArgsConstructor
public class LolChampionSyncController {

    private final LolChampionSyncService lolChampionSyncService;

    @PostMapping("/sync")
    @Operation(summary = "TFT 챔피언 동기화", description = "lolchess champion_refs 데이터를 읽어 챔피언 메타데이터를 DB에 동기화합니다.")
    public ResponseEntity<ApiResponse<LolChampionSyncResponse>> syncChampionRefs() {
        LolChampionSyncResponse response = lolChampionSyncService.syncChampionRefs();
        return ResponseEntity.ok(new ApiResponse<>(true, "lol champions synced", response));
    }
}
