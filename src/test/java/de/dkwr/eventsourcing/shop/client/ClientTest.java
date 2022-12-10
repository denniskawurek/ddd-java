package de.dkwr.eventsourcing.shop.client;

import de.dkwr.eventsourcing.shop.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class ClientTest {
    @Test
    public void clientCreationTest() {
        String name = "Alexander Humboldt";
        String phone = "0049555120012";

        Client client = new Client(name, phone);

        Assertions.assertEquals(0, client.getVersion());
        Assertions.assertEquals(1, client.getAppliedNewEvents().size());
        Assertions.assertEquals(name, client.getName());
        Assertions.assertEquals(phone, client.getPhone());
    }

    @Test
    public void clientCreationAndUpdateTest() {
        String name = "Alexander Humboldt";
        String phone = "0049555120012";

        Client client = new Client(name, phone);

        String editedName = "Wilhelm Humboldt";

        client.update(editedName, phone);

        Assertions.assertEquals(0, client.getVersion());
        Assertions.assertEquals(2, client.getAppliedNewEvents().size());
        Assertions.assertEquals(editedName, client.getName());
        Assertions.assertEquals(phone, client.getPhone());
    }

    @Test
    public void clientRestoredFromEventsAndUpdatedTest() {
        UUID aggregateId = UUID.randomUUID();
        List<Event> events = new LinkedList<>();

        String name = "Alexander Humboldt";
        String phone = "0049555120012";
        ClientEvent.ClientCreatedEvent clientCreatedEvent = new ClientEvent.ClientCreatedEvent(
                aggregateId, LocalDateTime.now(), 1, name, phone);


        Map<String, String> changedFields = new HashMap<>();
        changedFields.put("name", name);
        name = "Wilhelm Humboldt";
        ClientEvent.ClientUpdatedEvent clientUpdatedEvent = new ClientEvent.ClientUpdatedEvent(
                aggregateId, LocalDateTime.now(), 2, name, phone, changedFields);

        events.add(clientCreatedEvent);
        events.add(clientUpdatedEvent);

        Client client = new Client(aggregateId, events);

        Assertions.assertEquals(2, client.getVersion());
        Assertions.assertEquals(name, client.getName());
        Assertions.assertEquals(phone, client.getPhone());
        Assertions.assertEquals(0, client.getAppliedNewEvents().size());

        phone = "00475551233199";
        client.update(name, phone);

        Assertions.assertEquals(phone, client.getPhone());
        Assertions.assertEquals(2, client.getVersion());
        Assertions.assertEquals(name, client.getName());
        Assertions.assertEquals(1, client.getAppliedNewEvents().size());
    }

    @Test
    public void reverseEventTest() {
        UUID aggregateId = UUID.randomUUID();
        List<Event> events = new LinkedList<>();

        String nameBeforeUpdate = "Alexander Humboldt";
        String phone = "0049555120012";
        ClientEvent.ClientCreatedEvent clientCreatedEvent = new ClientEvent.ClientCreatedEvent(
                aggregateId, LocalDateTime.now(), 1, nameBeforeUpdate, phone);


        Map<String, String> changedFields = new HashMap<>();
        changedFields.put("name", nameBeforeUpdate);
        String nameAfterUpdate = "Wilhelm Humboldt";
        ClientEvent.ClientUpdatedEvent clientUpdatedEvent = new ClientEvent.ClientUpdatedEvent(
                aggregateId, LocalDateTime.now(), 2, nameAfterUpdate, phone, changedFields);

        events.add(clientCreatedEvent);
        events.add(clientUpdatedEvent);

        Client client = new Client(aggregateId, events);

        client.reverseUpdate(clientUpdatedEvent.getVersion(), clientUpdatedEvent.getOldValuesBeforeChange());

        Assertions.assertEquals(1, client.getAppliedNewEvents().size());
        Assertions.assertEquals(nameBeforeUpdate, client.getName());

        Event appliedEvent = client.getAppliedNewEvents().get(0);
        Assertions.assertEquals(ClientEvent.Type.CLIENT_UPDATE_REVERSED, appliedEvent.getEventType());

        ClientEvent.ReverseClientUpdatedEvent reverseClientUpdatedEvent = (ClientEvent.ReverseClientUpdatedEvent) appliedEvent;
        Assertions.assertEquals(clientUpdatedEvent.getOldValuesBeforeChange().get("name"), reverseClientUpdatedEvent.getName());
        Assertions.assertEquals(clientUpdatedEvent.getPhone(), reverseClientUpdatedEvent.getPhone());
    }
}
