package com.example.demo.lol.dto;

import java.time.Instant;
import java.util.List;

public record LolMetaResponse(
        String sourceUrl,
        String season,
        Instant scrapedAt,
        Instant sourceUpdatedAt,
        List<LolMetaDeckDto> decks
) {
}
