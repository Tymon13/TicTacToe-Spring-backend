package com.example.demo;

public class PlayerStats {
    private final int id;
    private final int wins;
    private final int losses;
    private final int draws;

    public PlayerStats(int id, int wins, int losses, int draws) {
        this.id = id;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }
}
