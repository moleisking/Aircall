package com.aircall.AlertNotificationSystem;

import com.aircall.AlertNotificationSystem.services.PagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AlertNotificationSystemApplication {

	@Autowired 
	PagerService pagerService;

	public static void main(String[] args) {
		SpringApplication.run(AlertNotificationSystemApplication.class, args);
	}

}
