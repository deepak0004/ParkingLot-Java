package com.deepak.parkinglot;

// OBSERVER: reacts when a level becomes full. Add more observers (a gate,
// a dashboard, a logger) the same way - none of them touch Level's code.
public class FullSignBoard implements LevelObserver {
    @Override
    public void onLevelFull(Level level) {
        System.out.println("Level " + level.getLevel() + " is FULL");
    }
}
