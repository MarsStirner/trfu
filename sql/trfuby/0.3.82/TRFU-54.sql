INSERT INTO `trfu`.`trfu_classifiers` (`id`, `deleted`, `value`, `category`, `uuid`) VALUES (112, 0, 'ACD', 'Тип добавляемого вещества', null);
INSERT INTO `trfu`.`trfu_classifiers` (`id`, `deleted`, `value`, `category`, `uuid`) VALUES (113, 0, 'DMCO', 'Тип добавляемого вещества', null);
INSERT INTO `trfu`.`trfu_classifiers` (`id`, `deleted`, `value`, `category`, `uuid`) VALUES (114, 0, 'HAES 6 %', 'Тип добавляемого вещества', null);
INSERT INTO `trfu`.`trfu_classifiers` (`id`, `deleted`, `value`, `category`, `uuid`) VALUES (115, 0, 'HAES 10 %', 'Тип добавляемого вещества', null);
INSERT INTO `trfu`.`trfu_classifiers` (`id`, `deleted`, `value`, `category`, `uuid`) VALUES (116, 0, 'Плазма', 'Тип добавляемого вещества', null);
INSERT INTO `trfu`.`trfu_classifiers` (`id`, `deleted`, `value`, `category`, `uuid`) VALUES (117, 0, 'NaCl 0,9 %', 'Тип добавляемого вещества', null);
INSERT INTO `trfu`.`trfu_classifiers` (`id`, `deleted`, `value`, `category`, `uuid`) VALUES (118, 0, 'Псорален (8 МОР)', 'Тип добавляемого вещества', null);

ALTER TABLE `trfu`.`trfu_medical_biomaterial_additional_substances` CHANGE COLUMN `volume` `volume` DOUBLE NOT NULL;
ALTER TABLE `trfu`.`trfu_medical_biomaterials` CHANGE COLUMN `volume` `volume` DOUBLE NOT NULL;
ALTER TABLE `trfu`.`trfu_medical_biomaterials` CHANGE COLUMN `initialVolume` `initialVolume` DOUBLE NOT NULL;

