package com.aircall.AlertNotificationSystem.models;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MonitoredService {

    @Id
    @GeneratedValue  
    UUID id;

    String state;

    Long ack;    

    public Boolean isEmpty() {
        return id.toString().isEmpty() ? true : false;
    }

    public void setStatusToDown() {       
            this.state = "down";
            this.ack = System.currentTimeMillis()/1000;       
    }

    public void setStatusToUp() {     
            this.state = "up";
            this.ack = System.currentTimeMillis()/1000;        
    }

    public void toggleStatusAndAck() {
        if (this.state.equals("up")) {
            this.setStatusToDown();
        } else {
            this.setStatusToUp();
        }
    }
}
