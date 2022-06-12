package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "game_completion")
public class GameCompletionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @NonNull
    private GameStateEntity game;

    @OneToOne
    @NonNull
    private PlayerEntity winner;
}
