package com.deepak.parkinglot;

import java.util.concurrent.atomic.AtomicBoolean;

public class Spot {
    private final int spotId;
    private final SpotType spotType;
    private final AtomicBoolean taken = new AtomicBoolean(false);

    public Spot(int spotId, SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
    }

    public int getSpotId() {
        return spotId;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public boolean isAvailable() {
        return !taken.get();
    }

    /**
     * Atomically claims this spot. Returns {@code true} only for the single
     * caller that flips it from free to taken; concurrent callers get
     * {@code false}. This is the check-then-act done atomically.
     */
    public boolean claim() {
        return taken.compareAndSet(false, true);
    }

    /** Frees the spot so it can be claimed again. */
    public void release() {
        taken.set(false);
    }
}
