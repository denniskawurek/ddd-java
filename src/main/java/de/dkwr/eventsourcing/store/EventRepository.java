package de.dkwr.eventsourcing.store;

import de.dkwr.eventsourcing.shop.Event;

import java.util.List;
import java.util.UUID;

public interface EventRepository {
    List<Event> findById(UUID aggregateId);

    void save(UUID aggregateId, long version, List<Event> events);
}
