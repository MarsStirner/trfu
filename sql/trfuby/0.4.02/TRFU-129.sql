ALTER TABLE `trfu_blood_components`
	ADD COLUMN `virusInactivationDate` DATETIME NULL DEFAULT NULL COMMENT 'Дата проведения вирусинактивации' AFTER `bigLabelPath`;
-- Добавление администратору роли вирусинактивации без выкидывания ошибок, если роль уже есть
INSERT INTO `person_roles` (`person_id`, `role_id`)
SELECT *
FROM (
	SELECT p.id AS 'A', r.id AS 'B'
	FROM Person p, Roles r
	WHERE p.login='Administrator'
	AND r.roleType = 'VIRUSINACTIVATION'
	LIMIT 1
) as tmp
WHERE NOT EXISTS (
	SELECT pr.*
	FROM `person_roles` pr
	INNER JOIN Person p ON p.id = pr.person_id
	INNER JOIN Roles r ON r.id = pr.role_id
	WHERE p.login='Administrator'
	AND r.roleType = 'VIRUSINACTIVATION'
	) LIMIT 1;