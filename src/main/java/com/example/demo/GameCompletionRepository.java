package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameCompletionRepository extends JpaRepository<GameCompletionEntity, Integer> {
    List<GameCompletionEntity> getAllByGameIn(List<GameStateEntity> games);
}
