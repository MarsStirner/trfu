ALTER TABLE `trfu_blood_components`
	ADD COLUMN `virusInactivationDate` DATETIME NULL DEFAULT NULL COMMENT 'Дата проведения вирусинактивации' AFTER `bigLabelPath`;