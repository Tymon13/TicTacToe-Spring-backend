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
@Table(name = "game_state")
public class GameStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @NonNull
    private byte[] data;

    @OneToOne
    @NonNull
    private PlayerEntity player1;

    @OneToOne
    @NonNull
    private PlayerEntity player2;

    @OneToOne
    private GameCompletionEntity completion;
}
