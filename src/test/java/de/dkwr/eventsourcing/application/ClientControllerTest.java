package de.dkwr.eventsourcing.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@SpringBootTest
public class ClientControllerTest {
    @Autowired
    ClientController clientController;

    @Test
    public void createClientTest() {
        String name = "Alexander Humboldt";
        String phone = "0555123000";
        ResponseEntity<ClientDTO> response = createClient(name, phone);

        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
    }

    @Test
    public void clientNotFoundTest() {
        String randomUUID = UUID.randomUUID().toString();
        ResponseEntity<ClientDTO> response = clientController.getClient(randomUUID);

        Assertions.assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void modifyClientTest() {
        String name = "Alexander Humboldt";
        String phone = "0555123000";
        ResponseEntity<ClientDTO> createClientResponse = createClient(name, phone);

        Assertions.assertEquals(200, createClientResponse.getStatusCode().value());
        Assertions.assertNotNull(createClientResponse.getBody());
        Assertions.assertNotNull(createClientResponse.getBody().getId());

        ClientDTO createdClient = createClientResponse.getBody();
        String clientId = createdClient.getId();

        String modifiedName = "Wilhelm Humboldt";
        ClientDTO modifiedClient = new ClientDTO(clientId, modifiedName, phone);

        ResponseEntity<ClientDTO> modifyResponse = clientController.modifyClient(clientId, modifiedClient);
        Assertions.assertEquals(200, modifyResponse.getStatusCode().value());
        Assertions.assertNotNull(modifyResponse.getBody());
        Assertions.assertNotNull(modifyResponse.getBody().getId());
        Assertions.assertEquals(clientId, modifyResponse.getBody().getId());
        Assertions.assertEquals(modifiedName, modifyResponse.getBody().getName());
        Assertions.assertEquals(phone, modifyResponse.getBody().getPhone());

        ResponseEntity<ClientDTO> getClientResponse = clientController.getClient(clientId);
        Assertions.assertNotNull(getClientResponse.getBody());
        Assertions.assertNotNull(getClientResponse.getBody().getId());
        Assertions.assertEquals(clientId, getClientResponse.getBody().getId());
        Assertions.assertEquals(modifiedName, getClientResponse.getBody().getName());
        Assertions.assertEquals(phone, getClientResponse.getBody().getPhone());
    }

    public ResponseEntity<ClientDTO> createClient(String name, String phone) {
        ClientDTO clientDTO = new ClientDTO(null, name, phone);
        return clientController.createClient(clientDTO);
    }
}
