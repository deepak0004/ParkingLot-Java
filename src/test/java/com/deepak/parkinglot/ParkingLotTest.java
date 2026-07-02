package com.deepak.parkinglot;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ParkingLotTest {

    // 1. A bike gets a ticket for a matching (SMALL) spot.
    @Test
    void reserveParksVehicleInMatchingSpot() {
        ParkingLot lot = new ParkingLot(List.of(
                new Level(0, List.of(new Spot(0, SpotType.SMALL)))));

        Ticket ticket = lot.reserve(VehicleFactory.create(VehicleType.BIKE, 1));

        assertNotNull(ticket, "bike should get a ticket for the SMALL spot");
    }

    // 2. No matching spot -> reserve returns null (a bike can't use a BIG spot).
    @Test
    void reserveReturnsNullWhenNoSpotFits() {
        ParkingLot lot = new ParkingLot(List.of(
                new Level(0, List.of(new Spot(0, SpotType.BIG)))));

        Ticket ticket = lot.reserve(VehicleFactory.create(VehicleType.BIKE, 1));

        assertNull(ticket, "bike should not fit a BIG-only lot");
    }

    // 3. Under contention, spots are never double-booked: 5 bikes, 3 spots
    //    -> exactly 3 distinct spots used.
    @Test
    void concurrentReserveNeverDoubleBooksASpot() throws InterruptedException {
        ParkingLot lot = new ParkingLot(List.of(new Level(0, List.of(
                new Spot(0, SpotType.SMALL),
                new Spot(1, SpotType.SMALL),
                new Spot(2, SpotType.SMALL)))));

        List<Ticket> tickets = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Vehicle bike = VehicleFactory.create(VehicleType.BIKE, 1000 + i);
            Thread t = new Thread(() -> {
                Ticket ticket = lot.reserve(bike);
                if (ticket != null) {
                    synchronized (tickets) {
                        tickets.add(ticket);
                    }
                }
            });
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        long distinctSpots = tickets.stream()
                .map(t -> t.getSpot().getSpotId())
                .distinct()
                .count();
        assertEquals(3, distinctSpots, "3 spots should be filled, each exactly once");
    }
}
