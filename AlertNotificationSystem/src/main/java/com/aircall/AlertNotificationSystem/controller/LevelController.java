package com.aircall.AlertNotificationSystem.controller;

import java.util.ArrayList;

import com.aircall.AlertNotificationSystem.models.Level;
import com.aircall.AlertNotificationSystem.repositories.LevelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LevelController {

    @Autowired
    LevelRepository levelRepository;

    @RequestMapping("/level/list")
    public ResponseEntity<ArrayList<Level>> list() {
        ArrayList<Level> levels = new ArrayList<Level>();
        levelRepository.findAll().forEach(levels::add);
        return ResponseEntity.ok(levels);
    }

    @RequestMapping("/level/add")
    public ResponseEntity<String> add(@Validated @RequestBody Level level) {
        levelRepository.save(level);
        return ResponseEntity.ok("Service added");
    }

    @RequestMapping("/level/edit")
    public ResponseEntity<String> edit(@Validated @RequestBody Level level) {
        levelRepository.save(level);
        return ResponseEntity.ok("Service updated");
    }

    @RequestMapping("/level/delete")
    public ResponseEntity<String> delete(@Validated @RequestBody Level level) {
        levelRepository.delete(level);
        return ResponseEntity.ok("Service deleted");
    }
}