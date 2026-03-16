package com.example.demo.lol.dto;

import java.time.LocalDateTime;

public record LolChampionSyncResponse(
        int syncedCount,
        String season,
        LocalDateTime sourceUpdatedAt,
        LocalDateTime syncedAt
) {
}
