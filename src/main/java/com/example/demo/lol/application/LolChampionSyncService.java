package com.example.demo.lol.application;

import com.example.demo.lol.domain.entity.LolChampion;
import com.example.demo.lol.domain.entity.LolChampionBackup;
import com.example.demo.lol.domain.repository.LolChampionBackupRepository;
import com.example.demo.lol.domain.repository.LolChampionRepository;
import com.example.demo.lol.dto.LolChampionRefDto;
import com.example.demo.lol.dto.LolChampionSyncResponse;
import com.example.demo.lol.infrastructure.client.LolChessMetaClient;
import com.example.demo.lol.infrastructure.parser.ChampionRefsParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LolChampionSyncService {
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private final LolChessMetaClient lolChessMetaClient;
    private final ChampionRefsParser championRefsParser;
    private final LolChampionRepository lolChampionRepository;
    private final LolChampionBackupRepository lolChampionBackupRepository;

    @Transactional
    public LolChampionSyncResponse syncChampionRefs() {
        Document document = lolChessMetaClient.fetchMetaPage();
        Instant syncedAt = Instant.now();
        LocalDateTime syncedDateTime = toLocalDateTime(syncedAt);
        ChampionRefsParser.LolChampionRefSnapshot snapshot = championRefsParser.parse(document, syncedAt);
        LocalDateTime sourceUpdatedAt = toLocalDateTime(snapshot.sourceUpdatedAt());

        List<LolChampion> champions = snapshot.champions().stream()
                .map(champion -> upsertEntity(champion, snapshot.season(), sourceUpdatedAt, syncedDateTime))
                .toList();

        lolChampionRepository.saveAll(champions);

        return new LolChampionSyncResponse(
                champions.size(),
                snapshot.season(),
                sourceUpdatedAt,
                syncedDateTime
        );
    }

    private LolChampion upsertEntity(
            LolChampionRefDto champion,
            String season,
            LocalDateTime sourceUpdatedAt,
            LocalDateTime syncedAt
    ) {
        LolChampion entity = lolChampionRepository.findById(champion.championKey())
                .orElseGet(LolChampion::new);

        boolean isNew = entity.getChampionKey() == null;
        if (isNew) {
            entity.setChampionKey(champion.championKey());
            applySnapshot(entity, champion, season, sourceUpdatedAt);
            entity.setCreatedAt(syncedAt);
            entity.setUpdatedAt(syncedAt);
            entity.setSyncedAt(syncedAt);
            return entity;
        }

        if (hasChanged(entity, champion, season, sourceUpdatedAt)) {
            lolChampionBackupRepository.save(createBackup(entity, syncedAt));
            applySnapshot(entity, champion, season, sourceUpdatedAt);
            entity.setUpdatedAt(syncedAt);
        }

        entity.setSyncedAt(syncedAt);
        return entity;
    }

    private boolean hasChanged(
            LolChampion entity,
            LolChampionRefDto champion,
            String season,
            LocalDateTime sourceUpdatedAt
    ) {
        String championTraits = String.join(",", champion.championTraits());
        return !java.util.Objects.equals(entity.getChampionName(), champion.championName())
                || !java.util.Objects.equals(entity.getChampionImageUrl(), champion.championImageUrl())
                || !java.util.Objects.equals(entity.getChampionCost(), champion.championCost())
                || !java.util.Objects.equals(entity.getChampionRole(), champion.championRole())
                || !java.util.Objects.equals(entity.getChampionTraits(), championTraits)
                || !java.util.Objects.equals(entity.getSkillName(), champion.skillName())
                || !java.util.Objects.equals(entity.getSkillDescription(), champion.skillDescription())
                || !java.util.Objects.equals(entity.getSeason(), season);
    }

    private void applySnapshot(
            LolChampion entity,
            LolChampionRefDto champion,
            String season,
            LocalDateTime sourceUpdatedAt
    ) {
        entity.setChampionName(champion.championName());
        entity.setChampionImageUrl(champion.championImageUrl());
        entity.setChampionCost(champion.championCost());
        entity.setChampionRole(champion.championRole());
        entity.setChampionTraits(String.join(",", champion.championTraits()));
        entity.setSkillName(champion.skillName());
        entity.setSkillDescription(champion.skillDescription());
        entity.setSeason(season);
        entity.setSourceUpdatedAt(sourceUpdatedAt);
    }

    private LolChampionBackup createBackup(LolChampion entity, LocalDateTime backupCreatedAt) {
        LolChampionBackup backup = new LolChampionBackup();
        backup.setChampionKey(entity.getChampionKey());
        backup.setChampionName(entity.getChampionName());
        backup.setChampionImageUrl(entity.getChampionImageUrl());
        backup.setChampionCost(entity.getChampionCost());
        backup.setChampionRole(entity.getChampionRole());
        backup.setChampionTraits(entity.getChampionTraits());
        backup.setSkillName(entity.getSkillName());
        backup.setSkillDescription(entity.getSkillDescription());
        backup.setSeason(entity.getSeason());
        backup.setCreatedAt(entity.getCreatedAt());
        backup.setUpdatedAt(entity.getUpdatedAt());
        backup.setSyncedAt(entity.getSyncedAt());
        backup.setRegDate(backupCreatedAt);
        return backup;
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID);
    }
}
