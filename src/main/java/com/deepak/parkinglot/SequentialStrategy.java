package com.deepak.parkinglot;

import java.util.List;

public class SequentialStrategy implements ParkingStrategy {
    private boolean matches(VehicleType vehicleType, SpotType spotType) {
        return switch (vehicleType) {
            case CAR -> spotType == SpotType.MED;
            case BIKE -> spotType == SpotType.SMALL;
            case TRUCK -> spotType == SpotType.BIG;
        };
    }

    @Override
    public Spot findSpot(Vehicle vehicle, List<Level> levels) {
        for (Level level : levels) {
            for (Spot spot : level.getSpots()) {
                if (spot.isAvailable() && matches(vehicle.getVehicleType(), spot.getSpotType())) {
                    return spot;
                }
            }
        }
        return null;
    }
}
