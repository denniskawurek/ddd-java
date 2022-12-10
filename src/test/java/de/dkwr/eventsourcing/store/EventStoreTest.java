package de.dkwr.eventsourcing.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class EventStoreTest {
    @Autowired
    EventStore eventStores;

    @Test
    public void testStorage() {
        UUID aggregateId = UUID.randomUUID();
        List<Event> events = new LinkedList<>();

        events.add(new TestEvent(aggregateId, 1));

        eventStores.store(aggregateId, 1, events);

        List<Event> storedEvents = eventStores.fetchEvents(aggregateId);

        Assertions.assertEquals(storedEvents.size(), events.size());
    }

    private static class TestEvent extends Event {
        private final UUID aggregateId;
        private final int version;

        public TestEvent(UUID aggregateId, int version) {
            super(aggregateId, LocalDateTime.now(), version, "test-event");
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
