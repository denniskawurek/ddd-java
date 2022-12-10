package de.dkwr.eventsourcing.shop;

import de.dkwr.eventsourcing.store.Event;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;

public abstract class Aggregate {

    private UUID aggregateId;
    private List<Event> newEvents;

    private int version;

    protected Aggregate(UUID id) {
        this(id, emptyList());
    }

    protected Aggregate(@NonNull UUID aggregateId, @NonNull List<Event> events) {
        this.aggregateId = aggregateId;
        events.forEach(e -> {
            apply(e);
            this.version = e.getVersion();
        });
        this.newEvents = new ArrayList<>();
    }

    private void apply(Event event) {
        try {
            Method method = this.getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
