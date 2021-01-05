package com.aircall.AlertNotificationSystem.services;

import com.aircall.AlertNotificationSystem.controller.MonitoredServiceController;
import com.aircall.AlertNotificationSystem.models.MonitoredService;
import com.aircall.AlertNotificationSystem.repositories.MonitoredServiceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TimerService {

    @Value("${aircall.ackTimeout}")
    private Integer ackTimeout;

    @Autowired
    EscalationPolicyService escalationPolicyService;

    @Autowired
    MonitoredServiceRepository monitoredServiceRepository;

    @Autowired
    PagerService pagerService;

    Logger log = LoggerFactory.getLogger(PagerService.class.getName());

    @Autowired
    MonitoredServiceController monitoredServiceController;

    @Scheduled(fixedRate = 1000)
    public void checkMonitoredServiceStates() {
        try {
            monitoredServiceRepository.findAll().forEach(monitoredService -> {
                if (isAckTimeout(monitoredService)) {
                    log.info("MonitoredService ack timeout so resending to targets");
                    // Reset ack timestamp
                    monitoredService.setAck(System.currentTimeMillis() / 1000);
                    pagerService.updateMoniteredService(monitoredService);
                    escalationPolicyService.getEscalationPolicyAndNotifyTargets(monitoredService);
                }
            });
        } catch (Exception ex) {
            log.error("MonitoredService checkMonitoredServiceStates error {}", ex.getMessage());
        }
    }

    private Boolean isAckTimeout(MonitoredService monitoredService) {
        int gap = (int) (Math.abs((System.currentTimeMillis() / 1000) - monitoredService.getAck()));
        log.error("isAckTimeout by {} for {}", gap, monitoredService.getId());
        if (gap >= (60 * ackTimeout)) {
            return true;
        } else {
            return false;
        }
    }

}
