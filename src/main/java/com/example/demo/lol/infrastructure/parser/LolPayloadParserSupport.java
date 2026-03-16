package com.example.demo.lol.infrastructure.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class LolPayloadParserSupport {

    private static final String HTTPS_PREFIX = "https:";

    private LolPayloadParserSupport() {
    }

    static JsonNode readRoot(Document document, ObjectMapper objectMapper) {
        Element nextDataScript = document.selectFirst("script#__NEXT_DATA__");
        if (nextDataScript == null) {
            throw new IllegalStateException("lolchess meta page does not contain __NEXT_DATA__ payload");
        }

        try {
            return objectMapper.readTree(nextDataScript.data());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse lolchess __NEXT_DATA__ payload", e);
        }
    }

    static JsonNode findQuery(JsonNode queries, String queryName) {
        for (JsonNode query : queries) {
            JsonNode queryKey = query.path("queryKey");
            if (queryKey.isArray() && queryKey.size() > 0 && queryName.equals(queryKey.get(0).asText())) {
                return query;
            }
        }
        throw new IllegalStateException("lolchess payload does not contain query: " + queryName);
    }

    static List<String> readTextList(JsonNode node) {
        if (!node.isArray()) {
            return Collections.emptyList();
        }

        List<String> values = new ArrayList<>();
        for (JsonNode element : node) {
            values.add(element.asText());
        }
        return values;
    }

    static Integer readFirstCost(JsonNode costNode) {
        if (!costNode.isArray() || costNode.isEmpty()) {
            return null;
        }
        return costNode.get(0).asInt();
    }

    static Instant toInstant(JsonNode epochMillisNode) {
        if (!epochMillisNode.canConvertToLong()) {
            return null;
        }
        return Instant.ofEpochMilli(epochMillisNode.asLong());
    }

    static String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        if (url.startsWith("//")) {
            return HTTPS_PREFIX + url;
        }
        return url;
    }

    static String toDisplayName(String championKey) {
        if (championKey == null || championKey.isBlank()) {
            return championKey;
        }
        return championKey
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Z])([A-Z][a-z])", "$1 $2")
                .trim();
    }
}
