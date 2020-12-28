package com.intellibet.repository;

import com.intellibet.model.Bet;
import com.intellibet.model.Event;
import com.intellibet.model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.dateTime <= :dateTime and e.outcome IS NULL")
    List<Event> findEventsBeforeDateTime(@Param("dateTime") LocalDateTime dateTime);

    @Query("select e from Event e where e.dateTime > :now")
    List<Event> findEventsAfterDateTime(@Param("now") LocalDateTime now);

    @Query("select e from Event e where e.category like :category")
    List<Event> findByCategory(@Param("category") String category );

    @Query("select e from Event e where e.dateTime > :now and e.category = :category")
    List<Event> findEventsAfterDateTimeByCategory(@Param("now") LocalDateTime now,
                                                  @Param("category") EventCategory category);

    @Query("select e from Event e where e.dateTime > :now and (e.playerA = :search or e.playerB = :search)")
    List<Event> findEventsAfterDateTimeBySearch(@Param("now") LocalDateTime now,
                                                  @Param("search") String search);

}
