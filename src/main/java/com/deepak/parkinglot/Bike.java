package com.deepak.parkinglot;

public class Bike extends Vehicle {
    public Bike(int vehicleId) {
        super(vehicleId, VehicleType.BIKE);
    }

    public void park(ParkingLot parkingLot) {
        super.park(parkingLot);
    }
}
