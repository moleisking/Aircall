package com.aircall.AlertNotificationSystem.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Target {

    @Id
    @GeneratedValue  
    UUID id;
    
    private String type;

    public boolean isTypeValid() {
        return (type.equals("email") || type.equals("sms")) ? true : false;
    }

}
