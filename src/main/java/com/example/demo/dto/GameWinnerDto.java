package com.example.demo.dto;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameWinnerDto {
    @NotNull
    Integer gameId;

    @Nullable
    Integer playerId;
}
