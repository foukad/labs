package fr.ttis.npp.ipera.batch.infra.repository;

import fr.ttis.npp.ipera.batch.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
