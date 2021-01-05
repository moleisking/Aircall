package com.aircall.AlertNotificationSystem.services;

import com.aircall.AlertNotificationSystem.models.EscalationPolicy;
import com.aircall.AlertNotificationSystem.models.MonitoredService;
import com.aircall.AlertNotificationSystem.repositories.EscalationPolicyRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EscalationPolicyService {

    @Autowired
    private EscalationPolicyRepository escalationPolicyRepository;  
   
    @Autowired
    private PagerService pagerAdapter;  

    private Logger log = LoggerFactory.getLogger(EscalationPolicyService.class.getName());

    public void getEscalationPolicyAndNotifyTargets(MonitoredService monitoredService) {
        try {           
            EscalationPolicy escalationPolicy = escalationPolicyRepository
                    .findByMonitoredService(monitoredService.getId());           

            for (int i = 0; i < escalationPolicy.getLevels().size(); i++) {
                if (escalationPolicy.getLevels().get(i).isActive()) {
                    pagerAdapter.notifyTargets(escalationPolicy.getLevels().get(i), monitoredService);
                    escalationPolicy.bubbleUpLevelStateToActive();
                    updateEscalationPolicyAndLevels(escalationPolicy);
                    break;
                }
            }   
        } catch (Exception ex) {
            log.error("MonitoredService getEscalationPolicyAndNotifyTargets error {}", ex.getMessage());           
        }
    }    

    protected void updateEscalationPolicyAndLevels(EscalationPolicy escalationPolicy) {
        try {
            escalationPolicyRepository.save(escalationPolicy);             
            log.info("EscalationPolicy levels bubbledUp for {}", escalationPolicy.getMonitoredService().getId());
        } catch (IllegalArgumentException ex) {
            log.error("EscalationPolicy error in updateEscalationPolicy {}", ex.getMessage());
        }
    }
}