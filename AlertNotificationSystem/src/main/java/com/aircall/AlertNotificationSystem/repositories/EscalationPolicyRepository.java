package com.aircall.AlertNotificationSystem.repositories;

import java.util.UUID;

import com.aircall.AlertNotificationSystem.models.EscalationPolicy;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalationPolicyRepository extends CrudRepository<EscalationPolicy, UUID> {

    EscalationPolicy findFirstByMonitoredService(UUID id);

    @Query(nativeQuery = true, value = "SELECT Top 1 * FROM Escalation_Policy C WHERE C.Monitored_Service_id = :id")
    EscalationPolicy findByMonitoredService(@Param("id") UUID id);
}