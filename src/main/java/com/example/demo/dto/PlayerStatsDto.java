package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerStatsDto {
    private int id;
    private int wins;
    private int losses;
    private int draws;
}
