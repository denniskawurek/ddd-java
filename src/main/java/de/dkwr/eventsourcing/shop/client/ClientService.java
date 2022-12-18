package de.dkwr.eventsourcing.shop.client;

import de.dkwr.eventsourcing.shop.Event;
import de.dkwr.eventsourcing.shop.EventStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    @Autowired
    private EventStorageService eventStorage;

    public Client createClient(String name, String phone) {
        Client client = new Client(name, phone);
        eventStorage.storeEvents(client.getAggregateId(), client.getVersion(), client.getUncommittedEvents());
        return client;
    }

    public Optional<Client> loadClient(UUID clientId) {
        List<Event> events = eventStorage.fetchEvents(clientId);

        if (events.isEmpty()) {
            return Optional.empty();
        }

        Client client = new Client(clientId, events);

        return Optional.of(client);
    }

    public Optional<Client> updateClient(UUID clientId, String name, String phone) {
        List<Event> events = eventStorage.fetchEvents(clientId);

        if (events.isEmpty()) {
            return Optional.empty();
        }

        Client client = new Client(clientId, events);
        client.update(name, phone);

        eventStorage.storeEvents(clientId, client.getVersion(), client.getUncommittedEvents());

        return Optional.of(client);
    }
}
