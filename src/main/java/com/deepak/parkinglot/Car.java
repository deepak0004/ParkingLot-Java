package com.deepak.parkinglot;

public class Car extends Vehicle {
    public Car(int vehicleId) {
        super(vehicleId, VehicleType.CAR);
    }

    public void park(ParkingLot parkingLot) {
        super.park(parkingLot);
    }
}
