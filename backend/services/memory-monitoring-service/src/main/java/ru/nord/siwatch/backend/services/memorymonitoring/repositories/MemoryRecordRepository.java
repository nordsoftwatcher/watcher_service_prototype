package ru.nord.siwatch.backend.services.memorymonitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nord.siwatch.backend.services.memorymonitoring.entities.MemoryRecord;

public interface MemoryRecordRepository extends JpaRepository<MemoryRecord, Long> {
}
