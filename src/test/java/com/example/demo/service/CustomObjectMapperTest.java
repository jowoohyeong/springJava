package com.example.demo.service;

import com.example.demo.dto.ChampionDTO;
import com.example.demo.domain.ChampionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomObjectMapperTest {
    @DisplayName("id값 없이 DTO를 가지고 entity를 만들면, id는 77L")
    @Test
    void toEntityTest(){
        ChampionDTO championDTO = new ChampionDTO("rise", "bbakbbak2");

        ChampionEntity championEntity = CustomObjectMapper.MAPPER.toEntity(championDTO);
        ChampionEntity expectedEntity = new ChampionEntity(77L, "rise", "bbakbbak2");

        assertThat(championEntity).usingRecursiveComparison().isEqualTo(expectedEntity);
    }

    @Test
    void toDtoTest() {
        ChampionEntity championEntity = new ChampionEntity(888L, "fizz", "fish");

        ChampionDTO championDTO = CustomObjectMapper.MAPPER.toDto(championEntity);

        ChampionDTO expectVO = new ChampionDTO("fizz", "fish");
        assertThat(championDTO).usingRecursiveComparison().isEqualTo(expectVO);
    }


}