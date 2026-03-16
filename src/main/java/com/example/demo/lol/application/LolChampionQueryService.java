package com.example.demo.lol.application;

import com.example.demo.lol.domain.entity.LolChampion;
import com.example.demo.lol.domain.repository.LolChampionRepository;
import com.example.demo.lol.dto.LolChampionRefDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LolChampionQueryService {

    private final LolChampionRepository lolChampionRepository;

    public Map<String, LolChampionRefDto> findChampionIndex() {
        List<LolChampion> champions = lolChampionRepository.findAll();
        if (champions.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, LolChampionRefDto> championIndex = new LinkedHashMap<>();
        for (LolChampion champion : champions) {
            championIndex.put(champion.getChampionKey(), toDto(champion));
        }
        return championIndex;
    }

    private LolChampionRefDto toDto(LolChampion champion) {
        return new LolChampionRefDto(
                champion.getChampionKey(),
                champion.getChampionName(),
                champion.getChampionImageUrl(),
                champion.getChampionCost(),
                champion.getChampionRole(),
                parseTraits(champion.getChampionTraits()),
                champion.getSkillName(),
                champion.getSkillDescription()
        );
    }

    private List<String> parseTraits(String traits) {
        if (traits == null || traits.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(traits.split("\\s*,\\s*"))
                .filter(value -> !value.isBlank())
                .toList();
    }
}
