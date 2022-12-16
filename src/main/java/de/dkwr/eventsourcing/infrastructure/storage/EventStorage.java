package de.dkwr.eventsourcing.infrastructure.storage;

import de.dkwr.eventsourcing.shop.Event;
import de.dkwr.eventsourcing.shop.EventStorageService;
import de.dkwr.eventsourcing.infrastructure.storage.inmemory.InMemoryEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventStorage implements EventStorageService {
    @Autowired
    private InMemoryEventRepository eventStoreRepository;

    @Override
    public List<Event> fetchEvents(UUID aggregateId) {
        return eventStoreRepository.findById(aggregateId);
    }

    @Override
    public void storeEvents(UUID aggregateId, int version, List<Event> events) {
        eventStoreRepository.save(aggregateId, version, events);
    }
}
