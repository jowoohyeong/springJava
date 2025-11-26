package com.example.demo.service;

import com.example.demo.dto.ChampionDTO;
import com.example.demo.domain.ChampionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomObjectMapper extends EntityMapper<ChampionDTO, ChampionEntity> {
    CustomObjectMapper MAPPER = Mappers.getMapper(CustomObjectMapper.class);

    @Override
    @Mapping(target = "id", constant = "77L")
    ChampionEntity toEntity(final ChampionDTO dto);
}
