package com.aircall.AlertNotificationSystem.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private Logger log = LoggerFactory.getLogger(PagerService.class.getName());

    public void send(UUID monitoredServiceId, String message) {
        log.info("Sms sent to MonitoredService {} , {}" , monitoredServiceId , message);
    }

}
