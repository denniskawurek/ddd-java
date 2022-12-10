package de.dkwr.eventsourcing.shop.client;

import de.dkwr.eventsourcing.shop.Aggregate;
import de.dkwr.eventsourcing.shop.Event;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class Client extends Aggregate {
    private UUID aggregateId;
    private String name;
    private String phone;
    private final int version = 0;

    private final List<Event> appliedNewEvents = new ArrayList<>();

    public Client(String name, String phone) {
        ClientEvent.ClientCreatedEvent clientCreatedEvent = new ClientEvent.ClientCreatedEvent(
                UUID.randomUUID(), LocalDateTime.now(), nextVersion(), name, phone);
        applyNewEvent(clientCreatedEvent);
    }

    protected Client(UUID aggregateId, List<Event> events) {
        super(aggregateId, events);
    }

    public void update(String name, String phone) {
        Map<String, String> oldValuesBeforeChange = createMapOfOldValuesIfChanged(name, phone);
        ClientEvent.ClientUpdatedEvent clientUpdatedEvent = new ClientEvent.ClientUpdatedEvent(
                aggregateId, LocalDateTime.now(), nextVersion(), name, phone, oldValuesBeforeChange);
        applyNewEvent(clientUpdatedEvent);
    }

    public void reverseUpdate(int reversedEventVersion, Map<String, String> updatedFields) { // TODO: changedClientFields into a container which holds the map
        String name = updatedFields.getOrDefault("name", getName());
        String phone = updatedFields.getOrDefault("phone", getPhone());

        Map<String, String> oldValuesBeforeChange = createMapOfOldValuesIfChanged(name, phone);
        ClientEvent.ReverseClientUpdatedEvent reverseClientUpdatedEvent = new ClientEvent.ReverseClientUpdatedEvent(
                aggregateId, LocalDateTime.now(), nextVersion(), name, phone, oldValuesBeforeChange, reversedEventVersion
        );

        applyNewEvent(reverseClientUpdatedEvent);
    }

    private Map<String, String> createMapOfOldValuesIfChanged(String newName, String newPhone) {
        HashMap<String, String> valuesBeforeChangeMap = new HashMap<>();
        if (newName.compareTo(name) != 0) valuesBeforeChangeMap.put("name", name);
        if (newPhone.compareTo(phone) != 0) valuesBeforeChangeMap.put("phone", phone);

        return valuesBeforeChangeMap;
    }

    @Override
    protected void applyEvent(Event event) {
        switch ((ClientEvent.Type) event.getEventType()) {
            case CLIENT_CREATED -> applyClientCreatedEvent((ClientEvent.ClientCreatedEvent) event);
            case CLIENT_UPDATED -> applyClientUpdatedEvent((ClientEvent.ClientUpdatedEvent) event);
            case CLIENT_UPDATE_REVERSED -> applyReverseClientUpdateEvent((ClientEvent.ReverseClientUpdatedEvent) event);
        }
    }

    private void applyReverseClientUpdateEvent(ClientEvent.ReverseClientUpdatedEvent event) {
        log.info("Applying event: {}", event.getEventType());
        this.name = event.getName();
        this.phone = event.getPhone();
    }

    private void applyClientCreatedEvent(ClientEvent.ClientCreatedEvent event) {
        log.info("Applying event: {}", event.getEventType());
        this.name = event.getName();
        this.phone = event.getPhone();
    }

    private void applyClientUpdatedEvent(ClientEvent.ClientUpdatedEvent event) {
        log.info("Applying event: {}", event.getEventType());
        this.name = event.getName();
        this.phone = event.getPhone();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

}