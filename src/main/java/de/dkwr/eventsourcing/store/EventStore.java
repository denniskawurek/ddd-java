package de.dkwr.eventsourcing.store;

import de.dkwr.eventsourcing.store.inmemory.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventStore implements EventStoreService {
    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Override
    public List<Event> fetchEvents(UUID aggregateId) {
        return eventStoreRepository.findById(aggregateId);
    }

    @Override
    public void store(UUID aggregateId, long version, List<Event> events) {
        eventStoreRepository.save(aggregateId, version, events);
    }
}
