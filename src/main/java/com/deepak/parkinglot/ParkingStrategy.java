package com.deepak.parkinglot;

import java.util.List;

public interface ParkingStrategy {
    /**
     * Finds an available spot for the given vehicle across the supplied levels.
     *
     * @return the matching free spot, or {@code null} if none is available.
     */
    Spot findSpot(Vehicle vehicle, List<Level> levels);
}
