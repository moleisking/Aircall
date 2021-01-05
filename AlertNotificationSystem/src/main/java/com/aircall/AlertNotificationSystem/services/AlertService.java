package com.aircall.AlertNotificationSystem.services;

import com.aircall.AlertNotificationSystem.models.MonitoredService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
     
    @Autowired 
    PagerService pagerService;

    public void sendHealthyAlert(MonitoredService monitoredService){
        monitoredService.setStatusToUp();
        pagerService.recieveAlert(monitoredService);
     }

     public void sendSickAlert(MonitoredService monitoredService){
        monitoredService.setStatusToDown();
        pagerService.recieveAlert(monitoredService);
     }
}
