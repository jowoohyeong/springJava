package com.example.demo.lol.infrastructure.parser;

import com.example.demo.lol.dto.LolChampionRefDto;
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
public class ChampionRefsParser {

    private static final String CHAMPION_REFS_QUERY = "championRefs";

    private final ObjectMapper objectMapper;

    public ChampionRefsParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public LolChampionRefSnapshot parse(Document document, Instant scrapedAt) {
        JsonNode root = LolPayloadParserSupport.readRoot(document, objectMapper);
        JsonNode queries = root.path("props").path("pageProps").path("dehydratedState").path("queries");
        JsonNode championRefsQuery = LolPayloadParserSupport.findQuery(queries, CHAMPION_REFS_QUERY);
        JsonNode dataNode = championRefsQuery.path("state").path("data");

        return new LolChampionRefSnapshot(
                extractChampionRefDtos(dataNode.path("champions")),
                dataNode.path("season").asText(null),
                LolPayloadParserSupport.toInstant(championRefsQuery.path("state").path("dataUpdatedAt")),
                scrapedAt
        );
    }

    public Map<String, LolChampionRefDto> indexByChampionKey(List<LolChampionRefDto> champions) {
        if (champions == null || champions.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, LolChampionRefDto> championIndex = new LinkedHashMap<>();
        for (LolChampionRefDto champion : champions) {
            championIndex.put(champion.championKey(), champion);
        }
        return championIndex;
    }

    private List<LolChampionRefDto> extractChampionRefDtos(JsonNode championsNode) {
        if (!championsNode.isArray()) {
            return Collections.emptyList();
        }

        List<LolChampionRefDto> champions = new ArrayList<>();
        for (JsonNode champion : championsNode) {
            champions.add(new LolChampionRefDto(
                    champion.path("key").asText(),
                    champion.path("name").asText(),
                    LolPayloadParserSupport.normalizeUrl(champion.path("imageUrl").asText(null)),
                    LolPayloadParserSupport.readFirstCost(champion.path("cost")),
                    champion.path("role").asText(null),
                    LolPayloadParserSupport.readTextList(champion.path("traits")),
                    champion.path("skill").path("name").asText(null),
                    champion.path("skill").path("desc").asText(null)
            ));
        }
        return champions;
    }

    public record LolChampionRefSnapshot(
            List<LolChampionRefDto> champions,
            String season,
            Instant sourceUpdatedAt,
            Instant scrapedAt
    ) {
    }
}
