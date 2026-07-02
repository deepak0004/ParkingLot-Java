package com.deepak.parkinglot;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Ticket {
    private final int ticketId;
    private final Spot spot;
    private final Timestamp ts;

    public Ticket(int ticketId, Spot spot, Timestamp ts) {
        this.ticketId = ticketId;
        this.spot = spot;
        this.ts = ts;
    }
}
