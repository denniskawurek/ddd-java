package de.dkwr.eventsourcing.store;

import java.util.List;
import java.util.UUID;

public interface EventStoreRepositoryIF {
    List<Event> findById(UUID aggregateId);

    void save(UUID aggregateId, long version, List<Event> events);
}
