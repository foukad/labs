package fr.transactis.npp.ipera.dataprocessing.infra.repository;

import fr.transactis.npp.ipera.dataprocessing.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
