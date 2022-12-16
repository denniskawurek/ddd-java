package de.dkwr.eventsourcing.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class EventStorageServiceTest {
    @Autowired
    EventStorageService eventStorageService;

    @Test
    public void storeTest() {
        UUID aggregateId = UUID.randomUUID();
        List<Event> events = new LinkedList<>();
        events.add(new TestEvent(aggregateId, 1));
        eventStorageService.storeEvents(aggregateId, 1, List.copyOf(events));

        List<Event> storedEvents = eventStorageService.fetchEvents(aggregateId);
        Assertions.assertEquals(1, storedEvents.size());
    }

    @Test
    public void storeAndUpdateTest() {
        UUID aggregateId = UUID.randomUUID();

        List<Event> events = new LinkedList<>();
        events.add(new TestEvent(aggregateId, 1));
        eventStorageService.storeEvents(aggregateId, 1, List.copyOf(events));

        events.clear();
        events.add(new TestEvent(aggregateId, 2));
        events.add(new TestEvent(aggregateId, 3));
        eventStorageService.storeEvents(aggregateId, 3, List.copyOf(events));

        List<Event> storedEvents = eventStorageService.fetchEvents(aggregateId);
        Assertions.assertEquals(3, storedEvents.size());
    }

    @Test
    public void emptyResultIfNotExistsTest() {
        List<Event> result = eventStorageService.fetchEvents(UUID.randomUUID());
        Assertions.assertEquals(0, result.size());
    }

    private static class TestEvent extends Event {
        private final UUID aggregateId;
        private final int version;

        public enum Type implements EventType {
            TEST_EVENT
        }

        public TestEvent(UUID aggregateId, int version) {
            super(aggregateId, LocalDateTime.now(), version, Type.TEST_EVENT);
            this.aggregateId = aggregateId;
            this.version = version;
        }

        @Override
        public UUID getAggregateId() {
            return this.aggregateId;
        }

        @Override
        public int getVersion() {
            return this.version;
        }
    }
}
