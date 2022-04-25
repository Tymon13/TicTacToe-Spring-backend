package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameStateRepository extends JpaRepository<GameStateEntity, Integer> {
    List<GameStateEntity> findAllByPlayer1OrPlayer2(PlayerEntity player);
}
