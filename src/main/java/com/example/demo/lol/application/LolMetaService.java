package com.example.demo.lol.application;

import com.example.demo.lol.dto.LolMetaResponse;
import com.example.demo.lol.infrastructure.client.LolChessMetaClient;
import com.example.demo.lol.infrastructure.parser.ChampionRefsParser;
import com.example.demo.lol.infrastructure.parser.GuideDeckParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LolMetaService {

    private final LolChessMetaClient lolChessMetaClient;
    private final LolChampionQueryService lolChampionQueryService;
    private final ChampionRefsParser championRefsParser;
    private final GuideDeckParser guideDeckParser;

    public LolMetaResponse fetchMetaDecks() {
        Document document = lolChessMetaClient.fetchMetaPage();
        Instant scrapedAt = Instant.now();
        ChampionRefsParser.LolChampionRefSnapshot snapshot = championRefsParser.parse(document, scrapedAt);
        return guideDeckParser.parse(document, scrapedAt, championRefsParser.indexByChampionKey(snapshot.champions()));
    }

    public LolMetaResponse fetchMetaDecksForPage() {
        Document document = lolChessMetaClient.fetchMetaPage();
        Instant scrapedAt = Instant.now();
        return guideDeckParser.parse(document, scrapedAt, lolChampionQueryService.findChampionIndex());
    }
}
