package com.deepak.parkinglot;

public class Truck extends Vehicle {
    public Truck(int vehicleId) {
        super(vehicleId, VehicleType.TRUCK);
    }

    public void park(ParkingLot parkingLot) {
        super.park(parkingLot);
    }
}
