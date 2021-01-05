package com.aircall.AlertNotificationSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Arrays;

import com.aircall.AlertNotificationSystem.models.EscalationPolicy;
import com.aircall.AlertNotificationSystem.models.Level;
import com.aircall.AlertNotificationSystem.models.MonitoredService;
import com.aircall.AlertNotificationSystem.models.Target;
import com.aircall.AlertNotificationSystem.repositories.EscalationPolicyRepository;
import com.aircall.AlertNotificationSystem.repositories.LevelRepository;
import com.aircall.AlertNotificationSystem.repositories.MonitoredServiceRepository;
import com.aircall.AlertNotificationSystem.repositories.TargetRepository;
import com.aircall.AlertNotificationSystem.services.AlertService;
import com.aircall.AlertNotificationSystem.services.PagerService;
import com.aircall.AlertNotificationSystem.services.TimerService;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlertNotificationSystemApplicationTests {

	@Value("${aircall.ackTimeout}")
	private Integer ackTimeout;

	@Autowired
	EscalationPolicyRepository escalationPolicyRepository;

	@Autowired
	LevelRepository levelRepository;

	@Autowired
	MonitoredServiceRepository monitoredServiceRepository;

	@Autowired
	TargetRepository targetRepository;

	@Autowired
	AlertService alertAdapter;

	@Autowired
	PagerService pagerAdapter;

	@Autowired
	TimerService timeAdapter;

	Logger log = LoggerFactory.getLogger(AlertNotificationSystemApplicationTests.class.getName());

	@Test
	void GivenMonitoringServiceHealthy_WhenPagerRecievesSickAlert_ThenPagerNotifiesTargetsAnd15MinuteDelay() {

		Long timestamp = System.currentTimeMillis() / 1000;

		// Create monitoring service
		MonitoredService expectedMonitoredService = new MonitoredService();
		expectedMonitoredService.setStatusToUp();
		expectedMonitoredService.setAck(timestamp);
		monitoredServiceRepository.save(expectedMonitoredService);

		// Create escalation policy
		Target firstTarget = new Target();
		firstTarget.setType("email");
		// targetRepository.save(firstTarget);

		Target secondTarget = new Target();
		secondTarget.setType("sms");
		// targetRepository.save(secondTarget);

		Level firstLevel = new Level();
		firstLevel.setStateToActive();
		firstLevel.setTargets(new ArrayList<Target>(Arrays.asList(firstTarget)));
		// levelRepository.save(firstLevel);

		Level secondLevel = new Level();
		secondLevel.setStateToInactive();
		secondLevel.setTargets(new ArrayList<Target>(Arrays.asList(secondTarget)));
		// levelRepository.save(secondLevel);

		EscalationPolicy expectedEscalationPolicy = new EscalationPolicy();
		expectedEscalationPolicy.setMonitoredService(expectedMonitoredService);
		expectedEscalationPolicy.setLevels(new ArrayList<Level>(Arrays.asList(firstLevel, secondLevel)));
		escalationPolicyRepository.save(expectedEscalationPolicy);

		// Do test event
		alertAdapter.sendSickAlert(expectedMonitoredService);

		Optional<MonitoredService> actualMonitoredService = monitoredServiceRepository
				.findById(expectedMonitoredService.getId());

		assertEquals(expectedMonitoredService.getId(), actualMonitoredService.get().getId());
		assertTrue(Math.abs(actualMonitoredService.get().getAck() - timestamp) < 5);
		assertEquals(actualMonitoredService.get().getState(), "down");
		assertEquals(ackTimeout, 15);
	}

	@Test
	void GivenMonitoringServiceSickAndPagerNotRecievesAlertAndLastLevelNotNotified_WhenThePagerAckTimeout_ThenPagerNotifiesTargetsAnd15MinuteDelay() {

		Long timestamp = System.currentTimeMillis() / 1000;

		// Create monitoring service
		MonitoredService expectedMonitoredService = new MonitoredService();
		expectedMonitoredService.setStatusToDown();
		expectedMonitoredService.setAck(timestamp + (ackTimeout * 65)); // Current time plus 15 minutes
		monitoredServiceRepository.save(expectedMonitoredService);

		// Create escalation policy
		Target firstTarget = new Target();
		firstTarget.setType("email");
		// targetRepository.save(firstTarget);
		Target secondTarget = new Target();
		secondTarget.setType("sms");
		// targetRepository.save(secondTarget);

		Level firstLevel = new Level();
		firstLevel.setStateToActive();
		firstLevel.setTargets(new ArrayList<Target>(Arrays.asList(firstTarget)));
		// levelRepository.save(firstLevel);

		Level secondLevel = new Level();
		secondLevel.setStateToInactive();
		secondLevel.setTargets(new ArrayList<Target>(Arrays.asList(secondTarget)));
		// levelRepository.save(secondLevel);

		EscalationPolicy expectedEscalationPolicy = new EscalationPolicy();
		expectedEscalationPolicy.setMonitoredService(expectedMonitoredService);
		expectedEscalationPolicy.setLevels(new ArrayList<Level>(Arrays.asList(firstLevel, secondLevel)));
		escalationPolicyRepository.save(expectedEscalationPolicy);

		// Do test event
		// alertAdapter.sendAlert(expectedMonitoredService) not done
		timeAdapter.checkMonitoredServiceStates();

		Optional<MonitoredService> actualMonitoredService = monitoredServiceRepository
				.findById(expectedMonitoredService.getId());

		Optional<EscalationPolicy> actualEscalationPolicy = escalationPolicyRepository
				.findById(expectedEscalationPolicy.getId());

		assertEquals(expectedMonitoredService.getId(), actualMonitoredService.get().getId());
		assertEquals(actualMonitoredService.get().getState(), "down");
		assertEquals(actualEscalationPolicy.get().getLevels().size(), 2);
		assertEquals(actualEscalationPolicy.get().getLevels().get(0).isActive(), false);
		assertEquals(actualEscalationPolicy.get().getLevels().get(1).isActive(), true);
		assertEquals(ackTimeout, 15);
	}

	/*@Test
	void GivenMonitoringServiceSicky_WhenPagerRecievesSickAlertAndTimeout_ThenPagerNotNotifiesTargets() {

		Long timestamp = System.currentTimeMillis() / 1000;

		MonitoredService expectedMonitoredService = new MonitoredService();
		expectedMonitoredService.setStatusToDown();
		expectedMonitoredService.setAck(timestamp + (ackTimeout * 65) );
		monitoredServiceRepository.save(expectedMonitoredService);

		// Create escalation policy
		Target firstTarget = new Target();
		firstTarget.setType("email");
		Target secondTarget = new Target();
		secondTarget.setType("sms");
		Level firstLevel = new Level();
		firstLevel.setStateToActive();
		firstLevel.setTargets(new ArrayList<Target>(Arrays.asList(firstTarget)));
		Level secondLevel = new Level();
		secondLevel.setStateToInactive();
		secondLevel.setTargets(new ArrayList<Target>(Arrays.asList(secondTarget)));

		EscalationPolicy expectedEscalationPolicy = new EscalationPolicy();
		expectedEscalationPolicy.setMonitoredService(expectedMonitoredService);
		expectedEscalationPolicy.setLevels(new ArrayList<Level>(Arrays.asList(firstLevel, secondLevel)));
		escalationPolicyRepository.save(expectedEscalationPolicy);

		// Do test event
		// alertAdapter.sendAlert(expectedMonitoredService) not done		
		timeAdapter.checkMonitoredServiceStates();

		Optional<MonitoredService> actualMonitoredService = monitoredServiceRepository
				.findById(expectedMonitoredService.getId());

		Optional<EscalationPolicy> actualEscalationPolicy = escalationPolicyRepository
				.findById(expectedEscalationPolicy.getId());

		assertEquals(expectedMonitoredService.getId(), actualMonitoredService.get().getId());
		assertEquals(actualMonitoredService.get().getState(), "down");
		assertEquals(actualEscalationPolicy.get().getLevels().size(), 2);
		assertEquals(actualEscalationPolicy.get().getLevels().get(0).isActive(), true);
		assertEquals(actualEscalationPolicy.get().getLevels().get(1).isActive(), false);
		assertEquals(ackTimeout, 15);
	}*/

	@Test
	void GivenMonitoringServiceSick_WhenPagerRecievesSickAlert_ThenMonitoringServiceSickAndPagerNotNotifiesTargetsAndNoDelay() {

		Long timestamp = System.currentTimeMillis() / 1000;

		MonitoredService expectedMonitoredService = new MonitoredService();
		expectedMonitoredService.setStatusToDown();
		expectedMonitoredService.setAck(timestamp);
		monitoredServiceRepository.save(expectedMonitoredService);

		// Create escalation policy
		Target firstTarget = new Target();
		firstTarget.setType("email");
		Target secondTarget = new Target();
		secondTarget.setType("sms");
		Level firstLevel = new Level();
		firstLevel.setStateToActive();
		firstLevel.setTargets(new ArrayList<Target>(Arrays.asList(firstTarget)));
		Level secondLevel = new Level();
		secondLevel.setStateToInactive();
		secondLevel.setTargets(new ArrayList<Target>(Arrays.asList(secondTarget)));

		EscalationPolicy expectedEscalationPolicy = new EscalationPolicy();
		expectedEscalationPolicy.setMonitoredService(expectedMonitoredService);
		expectedEscalationPolicy.setLevels(new ArrayList<Level>(Arrays.asList(firstLevel, secondLevel)));
		escalationPolicyRepository.save(expectedEscalationPolicy);

		// Do test event
		alertAdapter.sendSickAlert(expectedMonitoredService);

		Optional<MonitoredService> actualMonitoredService = monitoredServiceRepository
				.findById(expectedMonitoredService.getId());

		Optional<EscalationPolicy> actualEscalationPolicy = escalationPolicyRepository
				.findById(expectedEscalationPolicy.getId());

		assertEquals(expectedMonitoredService.getId(), actualMonitoredService.get().getId());
		assertEquals(actualMonitoredService.get().getState(), "down");
		assertEquals(actualEscalationPolicy.get().getLevels().size(), 2);
		assertEquals(actualEscalationPolicy.get().getLevels().get(0).isActive(), true);
		assertEquals(actualEscalationPolicy.get().getLevels().get(1).isActive(), false);
	}

	@Test
	void GivenMonitoringServiceSick_WhenPagerRecievesHealthyAlertAndAckTimeout_ThenMonitoringServiceHealthyAndPagerNotNotifiesTargetsAndNoDelay() {

		Long timestamp = System.currentTimeMillis() / 1000;

		MonitoredService expectedMonitoredService = new MonitoredService();
		expectedMonitoredService.setStatusToDown();
		expectedMonitoredService.setAck(timestamp);
		monitoredServiceRepository.save(expectedMonitoredService);

		// Create escalation policy
		Target firstTarget = new Target();
		firstTarget.setType("email");
		Target secondTarget = new Target();
		secondTarget.setType("sms");
		Level firstLevel = new Level();
		firstLevel.setStateToActive();
		firstLevel.setTargets(new ArrayList<Target>(Arrays.asList(firstTarget)));
		Level secondLevel = new Level();
		secondLevel.setStateToInactive();
		secondLevel.setTargets(new ArrayList<Target>(Arrays.asList(secondTarget)));

		EscalationPolicy expectedEscalationPolicy = new EscalationPolicy();
		expectedEscalationPolicy.setMonitoredService(expectedMonitoredService);
		expectedEscalationPolicy.setLevels(new ArrayList<Level>(Arrays.asList(firstLevel, secondLevel)));
		escalationPolicyRepository.save(expectedEscalationPolicy);

		// Do test event
		alertAdapter.sendHealthyAlert(expectedMonitoredService);

		Optional<MonitoredService> actualMonitoredService = monitoredServiceRepository
				.findById(expectedMonitoredService.getId());

		Optional<EscalationPolicy> actualEscalationPolicy = escalationPolicyRepository
				.findById(expectedEscalationPolicy.getId());

		assertEquals(expectedMonitoredService.getId(), actualMonitoredService.get().getId());
		assertEquals(actualMonitoredService.get().getState(), "up");
		assertEquals(actualEscalationPolicy.get().getLevels().size(), 2);
		assertEquals(actualEscalationPolicy.get().getLevels().get(0).isActive(), false);
		assertEquals(actualEscalationPolicy.get().getLevels().get(1).isActive(), true);
	}

}
