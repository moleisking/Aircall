Insert into monitored_service (id, state, ack) values ('a0000000000000000000000000000001','up', 1609330500)
Insert into target (id, type) values ('a0000000000000000000000000000001','sms')
Insert into target (id, type) values ('a0000000000000000000000000000002','email')
Insert into level (id, state) values ('a0000000000000000000000000000001','active')
Insert into level (id, state) values ('a0000000000000000000000000000002','inactive')
Insert into level_targets (level_id, targets_id) values ('a0000000000000000000000000000001','a0000000000000000000000000000001')
Insert into level_targets (level_id, targets_id) values ('a0000000000000000000000000000002','a0000000000000000000000000000002')
Insert into escalation_policy (id, monitored_service_id) values ('a0000000000000000000000000000001','a0000000000000000000000000000001')
Insert into escalation_policy_levels (escalation_policy_id, levels_id) values ('a0000000000000000000000000000001','a0000000000000000000000000000001')
Insert into escalation_policy_levels (escalation_policy_id, levels_id) values ('a0000000000000000000000000000001','a0000000000000000000000000000002')
                                     