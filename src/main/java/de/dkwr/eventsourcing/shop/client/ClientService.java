package de.dkwr.eventsourcing.shop.client;

import de.dkwr.eventsourcing.store.Event;
import de.dkwr.eventsourcing.store.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    @Autowired
    private EventStore eventStore;

    public Optional<Client> loadClient(UUID clientId) {
        List<Event> events = eventStore.fetchEvents(clientId);

        if (events.isEmpty()) {
            return Optional.empty();
        }

        Client client = new Client(clientId, events);

        return Optional.of(client);
    }

    public Optional<Client> updateClient(UUID clientId, String name, String phone) {
        List<Event> events = eventStore.fetchEvents(clientId);

        if (events.isEmpty()) {
            return Optional.empty();
        }

        Client client = new Client(clientId, events);
        client.update(name, phone);

        eventStore.store(clientId, client.getVersion(), events);

        return Optional.of(client);
    }
}
