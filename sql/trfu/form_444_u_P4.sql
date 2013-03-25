CREATE PROCEDURE form_444_u_P4 (IN startDate DATE)
BEGIN
  SELECT trfu_blood_components.`volume` AS trfu_blood_components_volume
     , trfu_blood_component_types.`value` AS trfu_blood_component_types_value
     , trfu_blood_components.`productionDate` AS trfu_blood_components_productionDate
     , trfu_blood_groups.`number` AS trfu_blood_groups_number
     , trfu_blood_donation_requests.`number` AS trfu_blood_donation_requests_number
     , trfu_donors.`lastName` AS trfu_donors_lastName
     , trfu_donors.`middleName` AS trfu_donors_middleName
     , trfu_donors.`firstName` AS trfu_donors_firstName
     , trfu_blood_components.`id` AS trfu_blood_components_id
     , trfu_classifiers.`value` AS trfu_classifiers_value
     , wf_history.`commentary` AS wf_history_commentary
     , wf_history.`startDate` AS wf_history_startDate
     , trfu_blood_components.donorCode AS donorCode
     , trfu_blood_components.purchased AS purchased
     , trfu_blood_components.number AS comp_number
FROM
  `wf_history` wf_history
INNER JOIN `trfu_blood_component_history` trfu_blood_component_history
ON wf_history.`id` = trfu_blood_component_history.`history_entry_id`
INNER JOIN `trfu_blood_components` trfu_blood_components
ON trfu_blood_component_history.`component_id` = trfu_blood_components.`id`
INNER JOIN `trfu_blood_component_types` trfu_blood_component_types
ON trfu_blood_components.`componentType_id` = trfu_blood_component_types.`id`
INNER JOIN `trfu_blood_donation_requests` trfu_blood_donation_requests
ON trfu_blood_components.`donationId` = trfu_blood_donation_requests.`id`
INNER JOIN `trfu_blood_groups` trfu_blood_groups
ON trfu_blood_components.`bloodGroup_id` = trfu_blood_groups.`id`
INNER JOIN `trfu_classifiers` trfu_classifiers
ON trfu_blood_components.`rhesusFactor_id` = trfu_classifiers.`id`
INNER JOIN `trfu_donors` trfu_donors
ON trfu_blood_donation_requests.`donor_id` = trfu_donors.`id`
INNER JOIN trfu_external_appointment_tests appoint_test
ON trfu_blood_donation_requests.appointment_id = appoint_test.appointment_id
INNER JOIN trfu_tests tests
ON tests.id = appoint_test.test_id
WHERE
  (wf_history.`to_status_id` = -1
  OR wf_history.`to_status_id` = -2)
  AND DATE(wf_history.`startDate`) = startDate
  AND tests.`value` LIKE '%Положительный%'
  AND tests.`type_id` IN (18, 19, 20, 21, 22)

UNION

SELECT trfu_blood_components.`volume` AS trfu_blood_components_volume
     , trfu_blood_component_types.`value` AS trfu_blood_component_types_value
     , trfu_blood_components.`productionDate` AS trfu_blood_components_productionDate
     , trfu_blood_groups.`number` AS trfu_blood_groups_number
     , trfu_blood_donation_requests.`number` AS trfu_blood_donation_requests_number
     , trfu_donors.`lastName` AS trfu_donors_lastName
     , trfu_donors.`middleName` AS trfu_donors_middleName
     , trfu_donors.`firstName` AS trfu_donors_firstName
     , trfu_blood_components.`id` AS trfu_blood_components_id
     , trfu_classifiers.`value` AS trfu_classifiers_value
     , wf_history.`commentary` AS wf_history_commentary
     , wf_history.`startDate` AS wf_history_startDate
     , trfu_blood_components.donorCode AS donorCode
     , trfu_blood_components.purchased AS purchased
     , trfu_blood_components.number AS comp_number
FROM
  `wf_history` wf_history
INNER JOIN `trfu_blood_component_history` trfu_blood_component_history
ON wf_history.`id` = trfu_blood_component_history.`history_entry_id`
INNER JOIN `trfu_blood_components` trfu_blood_components
ON trfu_blood_component_history.`component_id` = trfu_blood_components.`id`
INNER JOIN `trfu_blood_component_types` trfu_blood_component_types
ON trfu_blood_components.`componentType_id` = trfu_blood_component_types.`id`
INNER JOIN `trfu_blood_donation_requests` trfu_blood_donation_requests
ON trfu_blood_components.`donationId` = trfu_blood_donation_requests.`id`
INNER JOIN `trfu_blood_groups` trfu_blood_groups
ON trfu_blood_components.`bloodGroup_id` = trfu_blood_groups.`id`
INNER JOIN `trfu_classifiers` trfu_classifiers
ON trfu_blood_components.`rhesusFactor_id` = trfu_classifiers.`id`
INNER JOIN `trfu_donors` trfu_donors
ON trfu_blood_donation_requests.`donor_id` = trfu_donors.`id`
INNER JOIN `trfu_blood_donation_requests_trfu_tests` trfu_blood_donation_requests_trfu_tests
ON trfu_blood_donation_requests.`id` = trfu_blood_donation_requests_trfu_tests.`trfu_blood_donation_requests_id`
INNER JOIN `trfu_tests` trfu_tests
ON trfu_blood_donation_requests_trfu_tests.`tests_id` = trfu_tests.`id`
INNER JOIN `trfu_analysis_types` trfu_analysis_types
ON trfu_tests.`type_id` = trfu_analysis_types.`id`
WHERE
  (wf_history.`to_status_id` = -1
  OR wf_history.`to_status_id` = -2)
  AND DATE(wf_history.`startDate`) = startDate
  AND trfu_tests.`value` LIKE '%Положительный%'
  AND trfu_tests.`type_id` IN (18, 19, 20, 21, 22)
ORDER BY
  wf_history_startDate ASC;
END