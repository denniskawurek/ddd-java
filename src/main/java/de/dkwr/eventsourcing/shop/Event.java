package de.dkwr.eventsourcing.shop;

import de.dkwr.eventsourcing.shop.EventType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public abstract class Event {
    private final UUID aggregateId;
    private final LocalDateTime date;
    private final int version;
    private final EventType eventType;

    protected Event(UUID aggregateId, LocalDateTime date, int version, EventType eventType) {
        this.aggregateId = aggregateId;
        this.date = date;
        this.version = version;
        this.eventType = eventType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public int getVersion() {
        return version;
    }

    public EventType getEventType() { return eventType; }
}
