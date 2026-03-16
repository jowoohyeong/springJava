package com.example.demo.lol.domain.repository;

import com.example.demo.lol.domain.entity.LolChampion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolChampionRepository extends JpaRepository<LolChampion, String> {
}
