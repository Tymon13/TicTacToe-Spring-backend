package com.example.demo.entity;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "game_state")
public class GameStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(length = 600)
    private String data;

    @Lob
    private byte[] d;

    @OneToOne
    @Nullable
    private GameCompletionEntity completion;

    @OneToOne
    private PlayerEntity player1;

    @OneToOne
    private PlayerEntity player2;
}
