package com.deepak.parkinglot;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Level {
    private final int level;
    private final List<Spot> spots;

    public Level(int level, List<Spot> spots) {
        this.level = level;
        this.spots = spots;
    }

    public boolean isFull() {
        return spots.stream().noneMatch(Spot::isAvailable);
    }

    // ----- Observer pattern: this Level is the SUBJECT -----
    // Watchers register here, and get told when the level fills up.

    private final List<LevelObserver> observers = new ArrayList<>();

    public void addObserver(LevelObserver observer) {
        observers.add(observer);
    }

    public void notifyIfFull() {
        if (isFull()) {
            for (LevelObserver observer : observers) {
                observer.onLevelFull(this);
            }
        }
    }
}
