package com.example.demo.lol.domain.repository;

import com.example.demo.lol.domain.entity.LolChampionBackup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolChampionBackupRepository extends JpaRepository<LolChampionBackup, Long> {
}
