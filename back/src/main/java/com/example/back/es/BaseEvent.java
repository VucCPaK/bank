package com.example.back.es;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseEvent {
    protected String aggregateId;

    public BaseEvent(String aggregateId) {
        this.aggregateId = aggregateId;
    }
}
