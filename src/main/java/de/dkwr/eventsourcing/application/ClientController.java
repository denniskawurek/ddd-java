package de.dkwr.eventsourcing.application;

import de.dkwr.eventsourcing.shop.client.Client;
import de.dkwr.eventsourcing.shop.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController()
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable String clientId) {
        Optional<Client> client = clientService.loadClient(UUID.fromString(clientId));
        if(client.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toClientDTO(client.get()));
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO payload) {
        Client client = clientService.createClient(payload.getName(), payload.getPhone());

        return ResponseEntity.ok(toClientDTO(client));
    }

    @PatchMapping("/{clientId}")
    public ResponseEntity<ClientDTO> modifyClient(@PathVariable String clientId, @RequestBody ClientDTO payload) {
        Optional<Client> client = clientService.updateClient(UUID.fromString(clientId), payload.getName(), payload.getPhone());

        if (client.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toClientDTO(client.get()));
    }

    private ClientDTO toClientDTO(Client client) {
        return new ClientDTO(client.getAggregateId().toString(), client.getName(), client.getPhone());
    }
}
