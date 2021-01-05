package com.aircall.AlertNotificationSystem.models;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Alert {

    @Id
    @GeneratedValue  
    UUID id;

    String message;
}
