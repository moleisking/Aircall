package com.aircall.AlertNotificationSystem.services;

import com.aircall.AlertNotificationSystem.models.Level;
import com.aircall.AlertNotificationSystem.models.MonitoredService;
import com.aircall.AlertNotificationSystem.repositories.MonitoredServiceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PagerService {

    @Autowired
    private EscalationPolicyService escalationPolicyAdapter;

    @Autowired
    private MailService mailAdapter;

    @Autowired
    private MonitoredServiceRepository monitoredServiceRepository;

    @Autowired
    private SmsService smsAdapter;

    @Value("${aircall.ackTimeout}")
    private Integer ackTimeout;

    private Logger log = LoggerFactory.getLogger(PagerService.class.getName());

    public void recieveAlert(MonitoredService monitoredService) {

        if (!monitoredService.isEmpty()) {
            // AlertService has already changed the state, so will be compared with previous
            MonitoredService previousMoniteredService = getMonitringServiceInCurrentState(monitoredService);
            if (isMonitredServiceStateDifferent(previousMoniteredService, monitoredService)) {
                log.warn("MonitoredService status changed {} {}", monitoredService.getId(), monitoredService.getState());
                monitoredService.setAck(System.currentTimeMillis() / 1000);
                updateMoniteredService(monitoredService);
                escalationPolicyAdapter.getEscalationPolicyAndNotifyTargets(monitoredService);
            } else {
                log.warn("MonitoredService state not changed {} {}", monitoredService.getId(), monitoredService.getState());
            }
        } else {
            log.warn("MonitoredService Object Empty {}", monitoredService);
        }
    }
 

    private Boolean isMonitredServiceStateDifferent(MonitoredService previousMoniteredService,
            MonitoredService currentMoniteredService) {
        if (!previousMoniteredService.getState().equals(currentMoniteredService.getState())) {
            return true;
        } else {
            return false;
        }
    }

    private MonitoredService getMonitringServiceInCurrentState(MonitoredService monitoredService) {
        return monitoredServiceRepository.findById(monitoredService.getId()).get();
    }

    public void notifyTargets(Level level, MonitoredService monitoredService) {
        level.getTargets().forEach(target -> {
            if (target.getType().equals("email")) {
                mailAdapter.send(monitoredService.getId(), "my email message");
                log.info("Email sent to MonitoredService:", monitoredService.getId());
            } else if (target.getType().equals("sms")) {
                smsAdapter.send(monitoredService.getId(), "my sms message");
            } else {
                log.error("Unknown message adapter {} , {}", monitoredService.getId(), target.getType());
            }
        });
    }

    protected void updateMoniteredService(MonitoredService monitoredService) {
        try {
            monitoredServiceRepository.save(monitoredService);
            log.info("MonitoredService object toggled state for {}", monitoredService.getState());
        } catch (IllegalArgumentException ex) {
            log.error("MonitoredService Object Error in updateMoniteredServiceStateAndAck {}", ex);
        }
    }
}