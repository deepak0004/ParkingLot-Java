package com.deepak.parkinglot;

/** Notified when a level becomes full. */
public interface LevelObserver {
    void onLevelFull(Level level);
}
