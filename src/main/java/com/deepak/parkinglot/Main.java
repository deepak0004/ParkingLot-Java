package com.deepak.parkinglot;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== Case 1: single-threaded =====");
        caseOneSingleThreaded();

        System.out.println("\n===== Case 2: multi-threaded =====");
        caseTwoMultiThreaded();
    }

    // Case 1 - park two bikes into a lot with one SMALL and one BIG spot.
    private static void caseOneSingleThreaded() {
        Level level0 = new Level(0, List.of(
                new Spot(0, SpotType.SMALL),
                new Spot(1, SpotType.BIG)
        ));
        ParkingLot lot = new ParkingLot(List.of(level0));

        Vehicle bike1 = VehicleFactory.create(VehicleType.BIKE, 123);
        printResult(bike1.getVehicleId(), lot.reserve(bike1));

        Vehicle bike2 = VehicleFactory.create(VehicleType.BIKE, 223);
        printResult(bike2.getVehicleId(), lot.reserve(bike2));
    }

    // Case 2 - 5 bikes park at the same time into a lot with only 3 spots.
    // Exactly 3 get a spot, 2 are rejected - and never the same spot twice.
    private static void caseTwoMultiThreaded() throws InterruptedException {
        Level level0 = new Level(0, List.of(
                new Spot(0, SpotType.SMALL),
                new Spot(1, SpotType.SMALL),
                new Spot(2, SpotType.SMALL)
        ));
        level0.addObserver(new FullSignBoard());
        ParkingLot lot = new ParkingLot(List.of(level0));

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int vehicleId = 1000 + i;
            Thread t = new Thread(() ->
                    printResult(vehicleId, lot.reserve(VehicleFactory.create(VehicleType.BIKE, vehicleId))));
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    private static void printResult(int vehicleId, Ticket ticket) {
        if (ticket == null) {
            System.out.println("No spot available for vehicle " + vehicleId);
        } else {
            System.out.println("Parked vehicle " + vehicleId
                    + " at spot " + ticket.getSpot().getSpotId()
                    + " (ticket " + ticket.getTicketId() + ")");
        }
    }
}
