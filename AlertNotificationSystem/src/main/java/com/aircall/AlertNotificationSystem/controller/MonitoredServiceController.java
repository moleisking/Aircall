package com.aircall.AlertNotificationSystem.controller;

import java.util.ArrayList;

import com.aircall.AlertNotificationSystem.models.MonitoredService;
import com.aircall.AlertNotificationSystem.repositories.MonitoredServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoredServiceController {

    @Autowired
    MonitoredServiceRepository monitoredServiceRepository;

    @RequestMapping("/service/list")
    public ResponseEntity<ArrayList<MonitoredService>> list() {
        ArrayList<MonitoredService> monitoredServices = new ArrayList<MonitoredService>();
        monitoredServiceRepository.findAll().forEach(monitoredServices::add);
        return ResponseEntity.ok(monitoredServices);
    }

    @RequestMapping("/service/add")
    public ResponseEntity<String> add(@Validated @RequestBody MonitoredService monitoredService) {
        monitoredServiceRepository.save(monitoredService);
        return ResponseEntity.ok("Service added");
    }

    @RequestMapping("/service/edit")
    public ResponseEntity<String> edit(@Validated @RequestBody MonitoredService monitoredService) {
        monitoredServiceRepository.save(monitoredService);
        return ResponseEntity.ok("Service updated");
    }

    @RequestMapping("/service/delete")
    public ResponseEntity<String> delete(@Validated @RequestBody MonitoredService monitoredService) {
        monitoredServiceRepository.delete(monitoredService);
        return ResponseEntity.ok("Service deleted");
    }
}
