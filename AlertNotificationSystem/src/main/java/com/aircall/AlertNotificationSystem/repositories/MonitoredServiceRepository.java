package com.aircall.AlertNotificationSystem.repositories;

import java.util.UUID;

import com.aircall.AlertNotificationSystem.models.MonitoredService;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoredServiceRepository extends CrudRepository<MonitoredService, UUID> {}