package com.aircall.AlertNotificationSystem.controller;

import java.util.ArrayList;

import com.aircall.AlertNotificationSystem.models.EscalationPolicy;
import com.aircall.AlertNotificationSystem.repositories.EscalationPolicyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EscalationPolicyController {

    @Autowired
    EscalationPolicyRepository escalationPolicyRepository;

    @RequestMapping("/home")
    public String index() {
        return "Welcome to the tutorial from Aircall!";
    }

    @RequestMapping("/policy/list")
    public ResponseEntity<ArrayList<EscalationPolicy>> list() {
        ArrayList<EscalationPolicy> escalationPolicies = new ArrayList<EscalationPolicy>();
        escalationPolicyRepository.findAll().forEach(escalationPolicies::add);
        return ResponseEntity.ok(escalationPolicies);
    }

    @RequestMapping("/policy/add")
    public ResponseEntity<String> add(@Validated @RequestBody EscalationPolicy escalationPolicy) {
        escalationPolicyRepository.save(escalationPolicy);
        return ResponseEntity.ok("Service added");
    }

    @RequestMapping("/policy/edit")
    public ResponseEntity<String> edit(@Validated @RequestBody EscalationPolicy escalationPolicy) {
        escalationPolicyRepository.save(escalationPolicy);
        return ResponseEntity.ok("Service updated");
    }

    @RequestMapping("/policy/delete")
    public ResponseEntity<String> delete(@Validated @RequestBody EscalationPolicy escalationPolicy) {
        escalationPolicyRepository.delete(escalationPolicy);
        return ResponseEntity.ok("Service deleted");
    }
}
