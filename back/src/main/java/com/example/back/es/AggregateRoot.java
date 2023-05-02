package com.example.back.es;


import com.example.back.events.EventType;
import com.example.back.exceptions.InvalidEventException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Data
@NoArgsConstructor
public abstract class AggregateRoot {

    protected String id;
    protected String type;
    protected final List<Event> changes = new ArrayList<>();

    public AggregateRoot(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public abstract void when(final Event event);

    public void apply(Event event) {
        this.validateEvent(event);
        when(event);
        changes.add(event);
    }

    public void raiseEvent(Event event) {
        this.validateEvent(event);
        when(event);
    }

    private void validateEvent(Event event) {
        if (Objects.isNull(event) || !event.getAggregateId().equals(this.id))
            throw new InvalidEventException(event.toString());
    }

    protected Event createEvent(EventType eventType, byte[] data, byte[] metaData) {
        return Event.builder()
                .id(UUID.randomUUID())
                .aggregateId(this.getId())
                .eventType(eventType)
                .data(Objects.isNull(data) ? new byte[]{} : data)
                .metaData(Objects.isNull(metaData) ? new byte[]{} : metaData)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
