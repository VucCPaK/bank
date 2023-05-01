package com.example.back.es;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseEvent {
    protected String aggregateID;

    public BaseEvent(String aggregateID) {
        this.aggregateID = aggregateID;
    }
}
