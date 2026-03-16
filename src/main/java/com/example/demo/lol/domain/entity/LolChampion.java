package com.example.demo.lol.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lol_champion")
@Getter
@Setter
@NoArgsConstructor
public class LolChampion {

    @Id
    @Column(name = "champion_key", nullable = false, length = 100)
    private String championKey;

    @Column(name = "champion_name", nullable = false, length = 100)
    private String championName;

    @Column(name = "image_url", length = 500)
    private String championImageUrl;

    @Column(name = "champion_cost")
    private Integer championCost;

    @Column(name = "champion_role", length = 100)
    private String championRole;

    @Column(name = "champion_traits", length = 1000)
    private String championTraits;

    @Column(name = "skill_name", length = 200)
    private String skillName;

    @Lob
    @Column(name = "skill_description")
    private String skillDescription;

    @Column(name = "season", length = 40)
    private String season;

    @Column(name = "source_updated_at")
    private LocalDateTime sourceUpdatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "synced_at", nullable = false)
    private LocalDateTime syncedAt;
}
