package com.example.demo.lol.dto;

import java.util.List;

public record LolChampionRefDto(
        String championKey,
        String championName,
        String championImageUrl,
        Integer championCost,
        String championRole,
        List<String> championTraits,
        String skillName,
        String skillDescription
) {
}
