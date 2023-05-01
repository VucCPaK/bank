package com.example.back.repositories;

import com.example.back.es.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select aggregateId from Event where aggregateId = :aggregateId")
    void handleConcurrency(@Param("aggregateId") String aggregateId);

    @Query("select e from Event e where e.aggregateId = :aggregateId")
    List<Event> findAllByAggregateId(@Param("aggregateId") String aggregateId);
}
