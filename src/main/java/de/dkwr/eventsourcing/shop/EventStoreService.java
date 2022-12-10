package de.dkwr.eventsourcing.shop;

import de.dkwr.eventsourcing.shop.Event;

import java.util.List;
import java.util.UUID;

public interface EventStoreService {
        List<Event> fetchEvents(UUID aggregateId);
        void storeEvent(UUID aggregateId, long version, List<Event> events);
}
