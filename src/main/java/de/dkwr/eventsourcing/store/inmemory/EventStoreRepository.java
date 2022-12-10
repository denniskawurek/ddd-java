package de.dkwr.eventsourcing.store.inmemory;

import de.dkwr.eventsourcing.store.Event;
import de.dkwr.eventsourcing.store.EventStoreRepositoryIF;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EventStoreRepository implements EventStoreRepositoryIF {
    // AggregateId -> EventList
    ConcurrentHashMap<UUID, List<Event>> storage = new ConcurrentHashMap<>();
    @Override
    public List<Event> findById(UUID aggregateId) {
        return storage.get(aggregateId);
    }
    public void save(UUID aggregateId, long version, List<Event> events) {
        storage.put(aggregateId, events);
    }
}
