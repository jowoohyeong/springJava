package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetaDTO {
    private String name;

    private String imageDiv;  //임시
    private List<SynergyVO> synergyList;

    private List<ChampionVO> championList;

}
