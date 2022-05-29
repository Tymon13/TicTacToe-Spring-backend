package com.example.demo.repository;

import com.example.demo.entity.GameCompletionEntity;
import com.example.demo.entity.GameStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameCompletionRepository extends JpaRepository<GameCompletionEntity, Integer> {
    List<GameCompletionEntity> getAllByGameIn(List<GameStateEntity> games);
}
