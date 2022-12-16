package de.dkwr.eventsourcing.infrastructure.storage.inmemory;

import de.dkwr.eventsourcing.infrastructure.storage.EventRepository;
import de.dkwr.eventsourcing.shop.Event;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Repository
public class InMemoryEventRepository implements EventRepository {
    // AggregateId -> EventList
    ConcurrentHashMap<UUID, List<Event>> storage = new ConcurrentHashMap<>();
    @Override
    public List<Event> findById(UUID aggregateId) {
        if (!storage.containsKey(aggregateId)) {
            return Collections.emptyList();
        }
        return List.copyOf(storage.get(aggregateId));
    }
    public void save(UUID aggregateId, long version, List<Event> events) {
        if (!storage.containsKey(aggregateId)) {
            storage.put(aggregateId, events);
            return;
        }

        appendEvents(aggregateId, events);
    }

    private void appendEvents(UUID aggregateId, List<Event> events) {
        List<Event> mergedList = Stream.concat(storage.get(aggregateId).stream(), events.stream()).toList();
        storage.put(aggregateId, mergedList);
    }
}
