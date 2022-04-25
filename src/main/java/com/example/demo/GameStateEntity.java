package com.example.demo;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "game_state")
public class GameStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(length = 100)
    private String data;

    @OneToOne
    private PlayerEntity player1;

    @OneToOne
    private PlayerEntity player2;
}
