package ru.nord.siwatch.backend.services.events.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nord.siwatch.backend.services.events.entities.Event;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT event FROM Event event WHERE event.deviceTime>=?1 AND event.deviceTime<=?2 ORDER BY event.deviceTime DESC")
    List<Event> findAllInInterval(Date from, Date to);

    @Query("SELECT event FROM Event event WHERE event.deviceTime>=?1 ORDER BY event.deviceTime DESC")
    List<Event> findAllByFromDate(Date from);

    @Query("SELECT event FROM Event event WHERE event.deviceTime<=?1 ORDER BY event.deviceTime DESC")
    List<Event> findAllByToDate(Date to);

    @Query("SELECT event FROM Event event ORDER BY event.deviceTime DESC")
    List<Event> findAllEvents();

    @Query("SELECT event FROM Event event WHERE event.supervisorId =?1 AND event.deviceTime>=?2 AND event.deviceTime<=?3 ORDER BY event.deviceTime DESC")
    List<Event> findAllBySupervisorIdAndInInterval(Long supervisorId, Date from, Date to);

    @Query("SELECT event FROM Event event WHERE event.supervisorId =?1 AND event.deviceTime>=?2 ORDER BY event.deviceTime DESC")
    List<Event> findAllBySupervisorIdAndFromDate(Long supervisorId, Date from);

    @Query("SELECT event FROM Event event WHERE event.supervisorId =?1 AND event.deviceTime<=?2 ORDER BY event.deviceTime DESC")
    List<Event> findAllBySupervisorIdAndToDate(Long supervisorId, Date to);

    @Query("SELECT event FROM Event event WHERE event.supervisorId =?1 ORDER BY event.deviceTime DESC")
    List<Event> findAllBySupervisor(Long supervisorId);

    Optional<Event> findFirstByEventTypeAndSupervisorIdOrderByDeviceTimeDesc(String eventType, Long supervisorId);
}
