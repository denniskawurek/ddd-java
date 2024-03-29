package de.dkwr.eventsourcing.shop;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class Aggregate {

    private final List<Event> uncommittedEvents = new ArrayList<>();
    private UUID aggregateId;
    private int version;
    protected Aggregate() {}
    protected Aggregate(UUID aggregateId, List<Event> events) {
        this.aggregateId = aggregateId;
        restoreFromEvents(events);
    }

    private void restoreFromEvents(List<Event> events) {
        events.forEach(event -> {
            apply(event);
            this.version = event.getVersion();
        });
    }

    protected void applyNewEvent(Event event) {
        if(apply(event)) {
            uncommittedEvents.add(event);
        }
    }

    protected boolean apply(Event event) {
        if (!isNextEvent(event)) {
            log.error("Failed to apply event. This is not the next event. Current version: {} next version: {} Event version: {}",
                    version,
                    nextVersion(),
                    event.getVersion());
            return false;
        }
        processEvent(event);
        return true;
    }

    /**
     * Apply event based on type.
     * @param event The event to apply.
     */
    protected abstract void processEvent(Event event);

    public int getVersion() {
        return version;
    }

    private boolean isNextEvent(Event event) {
        return event.getVersion() == nextVersion();
    }

    protected int nextVersion() {
        return version + uncommittedEvents.size() + 1;
    }

    protected List<Event> getUncommittedEvents() {
        return List.copyOf(uncommittedEvents);
    }
}
