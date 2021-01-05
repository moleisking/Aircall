package com.aircall.AlertNotificationSystem.repositories;

import java.util.UUID;

import com.aircall.AlertNotificationSystem.models.Target;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepository extends CrudRepository<Target, UUID> {}