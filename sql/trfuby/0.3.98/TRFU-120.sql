ALTER TABLE `trfu`.`trfu_blood_components` 
ADD COLUMN 	`additionalVolume` INT(11) NOT NULL AFTER `uuid`,
ADD COLUMN	`additionalLiquor_id` INT(11) NULL DEFAULT NULL after `additionalVolume`,
ADD COLUMN	`bloodSystem_id` INT(11) NULL DEFAULT NULL after `additionalLiquor_id`;


CREATE	INDEX `FK56F2D4AD6151C161` ON `trfu`.`trfu_blood_components`(`bloodSystem_id`);
CREATE 	INDEX `FK56F2D4ADD444F476` ON `trfu`.`trfu_blood_components`(`additionalLiquor_id`);

ALTER TABLE `trfu`.`trfu_blood_components` ADD	CONSTRAINT `FK56F2D4AD6151C161` FOREIGN KEY (`bloodSystem_id`) REFERENCES `trfu_blood_systems` (`id`);
ALTER TABLE `trfu`.`trfu_blood_components` ADD	CONSTRAINT `FK56F2D4ADD444F476` FOREIGN KEY (`additionalLiquor_id`) REFERENCES `trfu_classifiers` (`id`);

ALTER TABLE `trfu`.`trfu_blood_components` ADD COLUMN `bigLabelPath` VARCHAR(255) NULL AFTER `bloodSystem_id`;