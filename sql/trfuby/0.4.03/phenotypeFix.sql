-- Замена в справочнике русских кодов на английские (руки-крюки поотрывать-бы)
UPDATE  `trfu_analysis_types` SET `value` = 'E' where `value` = 'Е' AND category = 'Иммуносерология';
UPDATE  `trfu_analysis_types` SET `value` = 'e' where `value` = 'е' AND category = 'Иммуносерология';
UPDATE  `trfu_analysis_types` SET `value` = 'C' where `value` = 'С' AND category = 'Иммуносерология';
UPDATE  `trfu_analysis_types` SET `value` = 'c' where `value` = 'с' AND category = 'Иммуносерология';