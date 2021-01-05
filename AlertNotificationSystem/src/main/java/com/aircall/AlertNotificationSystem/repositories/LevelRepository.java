package com.aircall.AlertNotificationSystem.repositories;

import java.util.UUID;

import com.aircall.AlertNotificationSystem.models.Level;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends CrudRepository<Level, UUID> {}
