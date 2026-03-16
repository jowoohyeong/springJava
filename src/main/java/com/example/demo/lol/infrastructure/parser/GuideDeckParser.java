package com.example.demo.lol.infrastructure.parser;

import com.example.demo.lol.dto.LolChampionRefDto;
import com.example.demo.lol.dto.LolMetaDeckDto;
import com.example.demo.lol.dto.LolMetaResponse;
import com.example.demo.lol.dto.LolMetaUnitDto;
import com.example.demo.lol.infrastructure.client.LolChessMetaClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GuideDeckParser {

    private static final String GUIDE_DECKS_QUERY = "getGuideDecks";

    private final ObjectMapper objectMapper;

    public GuideDeckParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public LolMetaResponse parse(Document document, Instant scrapedAt, Map<String, LolChampionRefDto> championRefs) {
        JsonNode root = LolPayloadParserSupport.readRoot(document, objectMapper);
        JsonNode queries = root.path("props").path("pageProps").path("dehydratedState").path("queries");
        JsonNode guideDeckQuery = LolPayloadParserSupport.findQuery(queries, GUIDE_DECKS_QUERY);
        JsonNode dataNode = guideDeckQuery.path("state").path("data");

        return new LolMetaResponse(
                LolChessMetaClient.LOLCHESS_META_URL,
                dataNode.path("season").asText(null),
                scrapedAt,
                LolPayloadParserSupport.toInstant(guideDeckQuery.path("state").path("dataUpdatedAt")),
                extractDecks(dataNode.path("guideDecks"), championRefs)
        );
    }

    private List<LolMetaDeckDto> extractDecks(JsonNode guideDecksNode, Map<String, LolChampionRefDto> championRefs) {
        if (!guideDecksNode.isArray()) {
            return Collections.emptyList();
        }

        List<LolMetaDeckDto> decks = new ArrayList<>();
        for (JsonNode guideDeck : guideDecksNode) {
            decks.add(new LolMetaDeckDto(
                    guideDeck.path("name").asText(),
                    guideDeck.path("teamBuilderKey").asText(),
                    guideDeck.path("cost").asInt(),
                    LolPayloadParserSupport.readTextList(guideDeck.path("augments")),
                    extractUnits(guideDeck.path("data").path("slots"), championRefs)
            ));
        }
        return decks;
    }

    private List<LolMetaUnitDto> extractUnits(JsonNode slotsNode, Map<String, LolChampionRefDto> championRefs) {
        if (!slotsNode.isArray()) {
            return Collections.emptyList();
        }

        Map<Integer, LolMetaUnitDto> orderedUnits = new LinkedHashMap<>();
        for (JsonNode slot : slotsNode) {
            String championKey = slot.path("champion").asText(null);
            if (championKey == null || championKey.isBlank()) {
                continue;
            }

            int boardIndex = slot.path("index").asInt(-1);
            LolChampionRefDto champion = championRefs.getOrDefault(championKey, fallbackChampion(championKey));
            orderedUnits.put(boardIndex, new LolMetaUnitDto(
                    boardIndex,
                    championKey,
                    champion.championName(),
                    champion.championImageUrl(),
                    champion.championCost(),
                    champion.championRole(),
                    champion.championTraits(),
                    champion.skillName(),
                    champion.skillDescription(),
                    slot.has("star") ? slot.path("star").asInt() : null,
                    LolPayloadParserSupport.readTextList(slot.path("items"))
            ));
        }
        return new ArrayList<>(orderedUnits.values());
    }

    private LolChampionRefDto fallbackChampion(String championKey) {
        return new LolChampionRefDto(
                championKey,
                LolPayloadParserSupport.toDisplayName(championKey),
                null,
                null,
                null,
                Collections.emptyList(),
                null,
                null
        );
    }
}
