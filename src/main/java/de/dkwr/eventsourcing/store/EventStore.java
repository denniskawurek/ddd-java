package de.dkwr.eventsourcing.store;

import de.dkwr.eventsourcing.shop.Event;
import de.dkwr.eventsourcing.shop.EventStoreService;
import de.dkwr.eventsourcing.store.inmemory.InMemoryEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventStore implements EventStoreService {
    @Autowired
    private InMemoryEventRepository eventStoreRepository;

    @Override
    public List<Event> fetchEvents(UUID aggregateId) {
        return eventStoreRepository.findById(aggregateId);
    }

    @Override
    public void storeEvent(UUID aggregateId, long version, List<Event> events) {
        eventStoreRepository.save(aggregateId, version, events);
    }
}
