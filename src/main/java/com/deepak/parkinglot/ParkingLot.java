package com.deepak.parkinglot;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLot {
    private final List<Level> levels;
    private final ParkingStrategy parkingStrategy;
    private final AtomicInteger nextTicketId = new AtomicInteger(1);
    private final Map<Integer, Vehicle> ticketToVehicle = new ConcurrentHashMap<>();
    private final Map<Integer, Ticket> vehicleToTicket = new ConcurrentHashMap<>();

    public ParkingLot(List<Level> levels) {
        this(levels, new SequentialStrategy());
    }

    public ParkingLot(List<Level> levels, ParkingStrategy parkingStrategy) {
        this.levels = levels;
        this.parkingStrategy = parkingStrategy;
    }

    /**
     * Parks the vehicle and returns its ticket, or {@code null} if the lot is
     * full for this vehicle type. Lock-free: each spot is claimed via an atomic
     * compare-and-set, so concurrent callers can never double-book a spot.
     */
    public Ticket reserve(Vehicle v) {
        // Fast path: already parked -> return the existing ticket (idempotent).
        Ticket existing = vehicleToTicket.get(v.getVehicleId());
        if (existing != null) {
            return existing;
        }

        while (true) {
            Spot spot = parkingStrategy.findSpot(v, levels);
            if (spot == null) {
                return null; // no spot fits this vehicle right now
            }
            if (!spot.claim()) {
                continue; // another thread grabbed it first; find another
            }

            Ticket t = new Ticket(nextTicketId.getAndIncrement(), spot,
                    new Timestamp(System.currentTimeMillis()));

            // Guard against two threads parking the same vehicle at once.
            Ticket won = vehicleToTicket.putIfAbsent(v.getVehicleId(), t);
            if (won != null) {
                spot.release();   // lost the dedup race; give the spot back
                return won;       // return whoever registered first
            }
            ticketToVehicle.put(t.getTicketId(), v);
            announceFullLevels(); // a spot was just taken - a level may now be full
            return t;
        }
    }

    /**
     * Releases the vehicle's spot. No-op if the ticket is not the vehicle's
     * current one (stale/foreign ticket, or already released).
     */
    public void unreserve(Vehicle v, Ticket t) {
        // Atomically remove only if t is still this vehicle's active ticket.
        if (!vehicleToTicket.remove(v.getVehicleId(), t)) {
            return;
        }
        ticketToVehicle.remove(t.getTicketId());
        t.getSpot().release();
    }

    // Lets each full level tell its observers. Levels that aren't full stay quiet.
    private void announceFullLevels() {
        for (Level level : levels) {
            level.notifyIfFull();
        }
    }

    public Ticket getTicket(int vehicleId) {
        return vehicleToTicket.get(vehicleId);
    }

    public Vehicle getParkedVehicle(int ticketId) {
        return ticketToVehicle.get(ticketId);
    }
}
