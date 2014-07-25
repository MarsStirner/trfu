INSERT INTO `trfu`.`roles` (`id`, `name`, `roleType`) VALUES ('14', 'Вирусинактивация', 'VIRUSINACTIVATION');

CREATE TABLE `trfu`.`trfu_dir_additionalLiquor` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(255) NULL DEFAULT NULL,
	`additionalVolume` INT(11) NOT NULL,
	`deleted` BIT(1) NOT NULL,
	`additionalLiquor_id` INT(11) NULL DEFAULT NULL,
	`componentType_id` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `componentType_id` (`componentType_id`),
	INDEX `FK2959F7AF48905E06` (`componentType_id`),
	INDEX `FK2959F7AFD444F476` (`additionalLiquor_id`),
	CONSTRAINT `FK2959F7AF48905E06` FOREIGN KEY (`componentType_id`) REFERENCES `trfu_blood_component_types` (`id`),
	CONSTRAINT `FK2959F7AFD444F476` FOREIGN KEY (`additionalLiquor_id`) REFERENCES `trfu_classifiers` (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=0;


INSERT INTO trfu.trfu_dir_additionalLiquor (`id`, `uuid`, `additionalVolume`, `componentType_id`, `additionalLiquor_id`, `deleted`) 
VALUES (1, null, 35, 108, 111, 0);

INSERT INTO trfu.trfu_dir_additionalLiquor (`id`, `uuid`, `additionalVolume`, `componentType_id`, `additionalLiquor_id`, `deleted`) 
VALUES (2, null, 35, 26, 111, 0);

INSERT INTO trfu.trfu_dir_additionalLiquor (`id`, `uuid`, `additionalVolume`, `componentType_id`, `additionalLiquor_id`, `deleted`) 
VALUES (3, null, 35, 28, 111, 0);

INSERT INTO trfu.trfu_dir_additionalLiquor (`id`, `uuid`, `additionalVolume`, `componentType_id`, `additionalLiquor_id`, `deleted`) 
VALUES (4, null, 35, 114, 111, 0);

INSERT INTO trfu.report_templates (`id`, `displayName`, `templateName`, `endAlias`, `startAlias`, `type`, `deleted`, `startDescription`, `endDescription`, `displayInFolders`, `uuid`) 
VALUES (57, 'Отчет по вирусинактивированным компонентам за день', 'virusinactiv_by_day.jrxml', null, 'FactDate', 0, 0, 'Дата', null, 1, null);

INSERT INTO trfu.report_templates (`id`, `displayName`, `templateName`, `endAlias`, `startAlias`, `type`, `deleted`, `startDescription`, `endDescription`, `displayInFolders`, `uuid`) 
VALUES (58, 'Отчет по вирусинактивированным компонентам за период', 'virusinactiv_by_period.jrxml', 'EndDate', 'StartDate', 1, 0, 'Начало периода', 'Конец периода', 1, null);
