package de.dkwr.eventsourcing.shop;

import java.util.List;
import java.util.UUID;

public interface EventStorageService {
        List<Event> fetchEvents(UUID aggregateId);
        void storeEvents(UUID aggregateId, int version, List<Event> events);
}
