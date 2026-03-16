package com.example.demo.lol.infrastructure.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LolChessMetaClient {

    public static final String LOLCHESS_META_URL = "https://lolchess.gg/meta";
    private static final int TIMEOUT_MILLIS = 10_000;
    private static final String USER_AGENT = "Mozilla/5.0";

    public Document fetchMetaPage() {
        try {
            return Jsoup.connect(LOLCHESS_META_URL)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MILLIS)
                    .get();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to fetch lolchess meta page", e);
        }
    }
}
