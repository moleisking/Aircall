package com.aircall.AlertNotificationSystem.models;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class EscalationPolicy {

    @Id
    @GeneratedValue
    UUID id;

    @OneToOne
    private MonitoredService monitoredService;

    @OneToMany(fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    private List<Level> levels;

    public void bubbleUpLevelStateToActive() {
        for (int i = 0; i < levels.size(); i++) {
            if (levels.size() == 1) {
                levels.get(i).setStateToActive();
                break;
            } else if ((i == (levels.size() - 1)) && (levels.get(i).isActive() == true)) {
                break;
            } else if ((i < (levels.size() - 1)) && (levels.get(i).isActive() == true)) {
                levels.get(i).setStateToInactive();
                levels.get(i + 1).setStateToActive();
            }
        }
    }

}
