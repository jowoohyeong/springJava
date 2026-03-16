package com.example.demo.lol.dto;

import java.util.List;

public record LolMetaDeckDto(
        String name,
        String teamBuilderKey,
        int cost,
        List<String> augments,
        List<LolMetaUnitDto> units
) {
}
