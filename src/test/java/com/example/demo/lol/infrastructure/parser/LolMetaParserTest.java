package com.example.demo.lol.infrastructure.parser;

import com.example.demo.lol.dto.LolMetaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class LolMetaParserTest {

    private final ChampionRefsParser championRefsParser = new ChampionRefsParser(new ObjectMapper());
    private final GuideDeckParser guideDeckParser = new GuideDeckParser(new ObjectMapper());

    @DisplayName("__NEXT_DATA__에서 가이드 덱과 챔피언 한글명을 추출한다")
    @Test
    void parseGuideDecks() {
        Document document = Jsoup.parse("""
                <html>
                <body>
                <script id="__NEXT_DATA__" type="application/json">
                {
                  "props": {
                    "pageProps": {
                      "dehydratedState": {
                        "queries": [
                          {
                            "queryKey": ["championRefs", "ko", "live"],
                            "state": {
                              "data": {
                                "season": "set16",
                                "champions": [
                                  {"key": "Aatrox", "name": "아트록스", "imageUrl": "//cdn.example.com/Aatrox.jpg", "cost": [5, 10, 20], "role": "전사", "traits": ["다르킨", "학살자"], "skill": {"name": "다르킨의 검", "desc": "강한 피해를 입힙니다."}},
                                  {"key": "Ahri", "name": "아리", "imageUrl": "https://cdn.example.com/Ahri.jpg"}
                                ]
                              },
                              "dataUpdatedAt": 1773631951385
                            }
                          },
                          {
                            "queryKey": ["getGuideDecks", "ko", "live"],
                            "state": {
                              "data": {
                                "season": "set16",
                                "guideDecks": [
                                  {
                                    "teamBuilderKey": "deck-1",
                                    "name": "테스트 덱",
                                    "cost": 42,
                                    "augments": ["TestAugment"],
                                    "data": {
                                      "slots": [
                                        {"index": 2, "champion": "Aatrox", "star": 2, "items": ["ItemA"]},
                                        {"index": 21, "champion": "Ahri", "star": 3, "items": ["ItemB", "ItemC"]}
                                      ]
                                    }
                                  }
                                ]
                              },
                              "dataUpdatedAt": 1773631951385
                            }
                          }
                        ]
                      }
                    }
                  }
                }
                </script>
                </body>
                </html>
                """);

        Instant scrapedAt = Instant.parse("2026-03-16T03:32:31Z");
        ChampionRefsParser.LolChampionRefSnapshot snapshot = championRefsParser.parse(document, scrapedAt);
        LolMetaResponse response = guideDeckParser.parse(document, scrapedAt, championRefsParser.indexByChampionKey(snapshot.champions()));

        assertThat(response.season()).isEqualTo("set16");
        assertThat(response.sourceUpdatedAt()).isEqualTo(Instant.ofEpochMilli(1773631951385L));
        assertThat(response.decks()).hasSize(1);
        assertThat(response.decks().get(0).name()).isEqualTo("테스트 덱");
        assertThat(response.decks().get(0).units()).hasSize(2);
        assertThat(response.decks().get(0).units().get(0).championName()).isEqualTo("아트록스");
        assertThat(response.decks().get(0).units().get(0).championImageUrl()).isEqualTo("https://cdn.example.com/Aatrox.jpg");
        assertThat(response.decks().get(0).units().get(0).championCost()).isEqualTo(5);
        assertThat(response.decks().get(0).units().get(0).championTraits()).containsExactly("다르킨", "학살자");
        assertThat(response.decks().get(0).units().get(0).skillName()).isEqualTo("다르킨의 검");
        assertThat(response.decks().get(0).units().get(1).items()).containsExactly("ItemB", "ItemC");
    }
}
