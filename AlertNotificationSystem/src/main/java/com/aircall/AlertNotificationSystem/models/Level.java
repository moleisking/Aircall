package com.aircall.AlertNotificationSystem.models;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Level {

    @Id
    @GeneratedValue
    UUID id;

    String state;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Target> targets;

    public Boolean isEmpty() {
        return id.toString().isEmpty() ? true : false;
    }

    public void setStateToActive() {
        this.state = "active";
    }

    public void setStateToInactive() {
        this.state = "inactive";
    }

    public Boolean isActive() {
        return this.state.equals("active") ? true : false;
    }

    public Boolean isInactive() {
        return this.state.equals("inactive") ? true : false;
    } 

}
