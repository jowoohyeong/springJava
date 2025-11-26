package com.example.demo.service;

import com.example.demo.domain.ChampionEntity;
import com.example.demo.dto.ChampionDTO;
import com.example.demo.dto.ChampionDTO.ChampionDTOBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T12:18:20+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
public class CustomObjectMapperImpl implements CustomObjectMapper {

    @Override
    public ChampionDTO toDto(ChampionEntity arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ChampionDTOBuilder championDTO = ChampionDTO.builder();

        championDTO.name( arg0.getName() );
        championDTO.icon( arg0.getIcon() );

        return championDTO.build();
    }

    @Override
    public ChampionEntity toEntity(ChampionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ChampionEntity championEntity = new ChampionEntity();

        championEntity.setName( dto.getName() );
        championEntity.setIcon( dto.getIcon() );

        championEntity.setId( (long) 77L );

        return championEntity;
    }
}
