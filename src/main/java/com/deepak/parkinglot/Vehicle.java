package com.deepak.parkinglot;

import lombok.Data;

@Data
public abstract class Vehicle {
    protected final int vehicleId;
    protected final VehicleType vehicleType;

    public Vehicle(int vehicleId, VehicleType vehicleType) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
    }

    protected void park(ParkingLot parkingLot) {
        parkingLot.reserve(this);
    }
}
