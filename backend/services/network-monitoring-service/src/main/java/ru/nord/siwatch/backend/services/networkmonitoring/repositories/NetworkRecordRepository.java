package ru.nord.siwatch.backend.services.networkmonitoring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nord.siwatch.backend.services.networkmonitoring.entities.NetworkRecord;

public interface NetworkRecordRepository extends JpaRepository<NetworkRecord, Long> {
}
