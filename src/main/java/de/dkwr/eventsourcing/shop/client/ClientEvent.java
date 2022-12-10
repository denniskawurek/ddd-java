package de.dkwr.eventsourcing.shop.client;

import de.dkwr.eventsourcing.store.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ClientEvent {
    public static class ClientCreatedEvent extends Event {
        @Getter
        private String name;
        @Getter
        private String phone;
        protected ClientCreatedEvent(UUID aggregateId, LocalDateTime date, int version, String name, String phone) {
            super(aggregateId, date, version, "client-created");

            this.name = name;
            this.phone = phone;
        }
    }
    public static class ClientUpdatedEvent extends Event {
        @Getter
        private String name;
        @Getter
        private String phone;
        @Getter
        private Map<String, String> changedFields;
        protected ClientUpdatedEvent(UUID aggregateId, LocalDateTime date, int version, String name, String phone, Map<String, String> changedFields) {
            super(aggregateId, date, version, "client-updated");

            this.name = name;
            this.phone = phone;
            this.changedFields = changedFields;
        }
    }

    public static class ReverseClientUpdatedEvent extends Event {
        @Getter
        private String name;
        @Getter
        private String phone;
        @Getter
        private Map<String, String> changedFields;
        @Getter
        private int reversedEventVersion;

        protected ReverseClientUpdatedEvent(UUID aggregateId, LocalDateTime date, int version, String name, String phone, Map<String, String> changedFields, int reversedEventVersion) {
            super(aggregateId, date, version, "client-update-reversed");

            this.name = name;
            this.phone = phone;
            this.changedFields = changedFields;
            this.reversedEventVersion = reversedEventVersion;
        }
    }
}
