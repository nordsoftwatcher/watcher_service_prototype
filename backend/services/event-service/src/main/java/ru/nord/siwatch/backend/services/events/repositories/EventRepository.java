package ru.nord.siwatch.backend.services.events.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nord.siwatch.backend.services.events.entities.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
