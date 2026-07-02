package com.deepak.parkinglot;

public class VehicleFactory {
    public static Vehicle create(VehicleType type, int vehicleId) {
        return switch (type) {
            case CAR -> new Car(vehicleId);
            case BIKE -> new Bike(vehicleId);
            case TRUCK -> new Truck(vehicleId);
        };
    }
}
