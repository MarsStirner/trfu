CREATE TABLE `trfu`.`trfu_blood_component_order_phenotype` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`uuid` VARCHAR(50) NOT NULL DEFAULT '0',
	`type` INT NOT NULL,
	`value` VARCHAR(50) NULL DEFAULT NULL,
	`bloodComponentOrder_id` INT NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK__trfu_blood_component_order_requests` (`bloodComponentOrder_id`),
	INDEX `FK__trfu_analysis_types` (`type`),
	CONSTRAINT `FK__trfu_analysis_types` FOREIGN KEY (`type`) REFERENCES `trfu_analysis_types` (`id`),
	CONSTRAINT `FK__trfu_blood_component_order_requests` FOREIGN KEY (`bloodComponentOrder_id`) REFERENCES `trfu_blood_component_order_requests` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

ALTER TABLE `trfu`.`trfu_blood_component_order_requests` 
	ADD COLUMN 	`kellAntigen_id` INT NULL DEFAULT NULL
	AFTER `recipientId`;
