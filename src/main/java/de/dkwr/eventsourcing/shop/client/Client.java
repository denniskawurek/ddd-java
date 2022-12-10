package de.dkwr.eventsourcing.shop.client;

import de.dkwr.eventsourcing.store.Event;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class Client {
    private UUID aggregateId;
    private String name;
    private String phone;
    private int version = 0;

    private final List<Event> appliedNewEvents = new LinkedList<>();

    public Client(String name, String phone) {
        ClientEvent.ClientCreatedEvent clientCreatedEvent = new ClientEvent.ClientCreatedEvent(
                UUID.randomUUID(), LocalDateTime.now(), nextVersion(), name, phone);
        applyNewEvent(clientCreatedEvent);
    }

    protected Client(UUID aggregateId, List<Event> events) {
        this.aggregateId = aggregateId;
        restoreFromEvents(events);
    }

    private void restoreFromEvents(List<Event> events) {
        events.forEach(event -> {
            this.apply(event);
            this.version = event.getVersion();
        });
    }

    public void update(String name, String phone) {
        Map<String, String> changedFields = getChangedFields(name, phone);
        ClientEvent.ClientUpdatedEvent clientUpdatedEvent = new ClientEvent.ClientUpdatedEvent(
                aggregateId, LocalDateTime.now(), nextVersion(), name, phone, changedFields);
        applyNewEvent(clientUpdatedEvent);
    }

    public void reverseUpdate(int reversedEventVersion, Map<String, String> updatedFields) { // TODO: changedClientFields into a container which holds the map
        String name = updatedFields.getOrDefault("name", getName());
        String phone = updatedFields.getOrDefault("phone", getPhone());

        Map<String, String> changedFields = getChangedFields(name, phone);
        ClientEvent.ReverseClientUpdatedEvent reverseClientUpdatedEvent = new ClientEvent.ReverseClientUpdatedEvent(
                aggregateId, LocalDateTime.now(), nextVersion(), name, phone, changedFields, reversedEventVersion
        );

        applyNewEvent(reverseClientUpdatedEvent);
    }

    private Map<String, String> getChangedFields(String newName, String newPhone) {
        HashMap<String, String> mapOfChangedValues = new HashMap<>();
        if (newName.compareTo(name) != 0) mapOfChangedValues.put("name", name);
        if (newPhone.compareTo(phone) != 0) mapOfChangedValues.put("phone", phone);

        return mapOfChangedValues;
    }

    private boolean apply(Event event) {
        if (!isNextEvent(event)) {
            log.error("Failed to apply event. This is not the next event. Current version: {} next version: {} Event version: {}",
                    version,
                    nextVersion(),
                    event.getVersion());
            return false;
        }
        applyEvent(event);
        return true;
    }

    public void applyNewEvent(Event event) {
        if(apply(event)) {
            appliedNewEvents.add(event);
        }
    }

    private boolean isNextEvent(Event event) {
        return event.getVersion() == nextVersion();
    }

    private void applyEvent(Event event) {
        switch (event.getEventType()) {
            case "client-created" -> applyClientCreatedEvent((ClientEvent.ClientCreatedEvent) event);
            case "client-updated" -> applyClientUpdatedEvent((ClientEvent.ClientUpdatedEvent) event);
            case "client-update-reversed" -> applyReverseClientUpdateEvent((ClientEvent.ReverseClientUpdatedEvent) event);
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



    private int nextVersion() {
        return version + appliedNewEvents.size() + 1;
    }

    public List<Event> getAppliedNewEvents() {
        return List.copyOf(appliedNewEvents);
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

}