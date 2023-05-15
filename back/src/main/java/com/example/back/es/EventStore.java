package com.example.back.es;

import com.example.back.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventStore {
    private final EventRepository eventRepository;
    private final EventBus eventBus;

    public <T extends AggregateRoot> void save(T aggregate) {
        List<Event> aggregateEvents = aggregate.getChanges();

        if (aggregateEvents.size() > 1) {
            this.handleConcurrency(aggregate.getId());
        }

        this.saveEvents(aggregateEvents);

        eventBus.publish(aggregateEvents);
        log.info("(save) saved aggregate: {}", aggregate);
    }

    public <T extends AggregateRoot> T load(String aggregateId, Class<T> clazz) {
        var aggregate = this.getAggregate(aggregateId, clazz);

        List<Event> events = this.loadEvents(aggregateId);
        events.forEach(aggregate::raiseEvent);

        log.info("(load) loaded aggregate: {}", aggregate);
        return aggregate;
    }

    public List<Event> loadEventsRelativeWithDepositOrWithdrawOperation(String aggregateId) {
        // todo check if need a sort by timestamp
        List<Event> events = eventRepository.findAllDepositOrWithdrawOperationEventByAggregateId(aggregateId);

        log.info("(loadEventsRelativeWithDepositOrWithdrawOperation) loaded withdraw/deposit operation events: {}", events);
        return events;
    }

    private  <T extends AggregateRoot> T getAggregate(String aggregateId, Class<T> clazz) {
        try {
            return clazz
                    .getConstructor(String.class)
                    .newInstance(aggregateId);

        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Event> loadEvents(String aggregateId) {
        List<Event> events = eventRepository.findAllByAggregateId(aggregateId);

        log.info("(loadEvents) loaded events: {}", events);
        return events;
    }

    private void saveEvents(List<Event> events) {
        if (events.isEmpty()) return;

        if (events.size() > 1) {
            this.eventsBatchInsert(events);
            return;
        }

        Event event = events.get(0);
        eventRepository.save(event);
        log.info("(saveEvents) saved event: {}", event);
    }

    private void eventsBatchInsert(List<Event> events) {
        eventRepository.saveAll(events);
        log.info("(eventsBatchInsert) saved events: {}", events);
    }

    private void handleConcurrency(String aggregateId) {
        eventRepository.handleConcurrency(aggregateId);
        log.info("(handleConcurrency) aggregateId for lock: {}", aggregateId);
    }

}
