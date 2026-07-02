# ParkingLot

A thread-safe parking lot simulation in Java, built around classic OO design patterns. Spots are claimed lock-free via atomic compare-and-set, so concurrent callers can never double-book a spot.

## Features

- **Lock-free reservation** - each spot is claimed with an atomic CAS; concurrent threads never double-book.
- **Idempotent parking** - reserving an already-parked vehicle returns its existing ticket.
- **Pluggable placement** - swap the spot-finding algorithm via the `ParkingStrategy` interface.
- **Full-level notifications** - observers are notified when a level fills up.

## Design

| Pattern | Where | Why |
|---------|-------|-----|
| Factory | `VehicleFactory` | Creates `Car` / `Bike` / `Truck` from a `VehicleType`. |
| Strategy | `ParkingStrategy`, `SequentialStrategy` | Decouples spot-selection from the lot. |
| Observer | `LevelObserver`, `FullSignBoard`, `Level` | React to a level becoming full without touching `Level`. |

### Model

- `ParkingLot` - top-level entry point; issues tickets and tracks vehicle <-> ticket mappings.
- `Level` - a floor holding many `Spot`s; acts as the observer subject.
- `Spot` - a single parking space; `claim()` atomically flips it from free to taken.
- `Vehicle` (`Car`, `Bike`, `Truck`) - vehicles matched to spots by type.
- `Ticket` - proof of reservation (id, spot, timestamp).

### Vehicle-to-spot matching (`SequentialStrategy`)

| Vehicle | Spot |
|---------|------|
| CAR | MED |
| BIKE | SMALL |
| TRUCK | BIG |

## Build & Run

Requires JDK 17+ and Maven.

```bash
# Run the tests
mvn clean test

# Run the demo (single-threaded and multi-threaded cases)
mvn compile exec:java -Dexec.mainClass=com.deepak.parkinglot.Main
```

`Main` runs two scenarios: parking bikes into a small lot, and 5 bikes racing for 3 spots concurrently (exactly 3 win, 2 are rejected, never the same spot twice).

## Project layout

```
src/main/java/com/deepak/parkinglot/   # source
src/test/java/com/deepak/parkinglot/   # JUnit 5 tests
```
