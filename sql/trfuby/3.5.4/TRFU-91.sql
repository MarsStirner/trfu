INSERT INTO `trfu`.`roles` (`id`, `name`, `roleType`) VALUES ('14', 'Вирусинактивация', 'VIRUSINACTIVATION');

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
