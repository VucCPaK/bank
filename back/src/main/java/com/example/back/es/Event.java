package com.example.back.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event", indexes = @Index(columnList = "aggregate_id"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Event {
    @Id
    UUID id;

    @Column(name = "aggregate_id",
            columnDefinition = "varchar(250) check (aggregate_id <> '')",
            nullable = false)
    String aggregateId;

    @Column(name = "event_type",
            columnDefinition = "varchar(250) check (aggregate_id <> '')",
            nullable = false)
    String eventType;

    @Column(name = "data",
            columnDefinition = "bytea",
            nullable = false)
    byte[] data;

    @Column(name = "meta_data",
            columnDefinition = "bytea",
            nullable = false)
    byte[] metaData;

    @Column(name = "timestamp",
            columnDefinition = "timestamp with time zone",
            nullable = false)
    LocalDateTime timestamp;
}
