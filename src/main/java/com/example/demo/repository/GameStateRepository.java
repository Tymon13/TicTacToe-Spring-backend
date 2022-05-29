package com.example.demo.repository;

import com.example.demo.entity.GameStateEntity;
import com.example.demo.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameStateRepository extends JpaRepository<GameStateEntity, Integer> {
    @Query("select g from GameStateEntity g where g.player1 = ?1 or g.player2 = ?1")
    List<GameStateEntity> findAllByPlayer1OrPlayer2(PlayerEntity player);
}
