package com.example.back.es;

import java.util.List;

public interface EventBus {
    void publish(List<Event> eventList);
}
