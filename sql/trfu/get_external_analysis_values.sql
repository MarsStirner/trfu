CREATE PROCEDURE get_external_analysis_values (IN appointment_id INT(10))
BEGIN

SELECT ext_res.* FROM trfu_external_appointment_analysis_entries ext_entry
INNER JOIN trfu_external_analysis_entry_results ext_entry_res ON ext_entry.entry_id = ext_entry_res.entry_id
INNER JOIN trfu_external_analysis_results ext_res ON ext_entry_res.result_id = ext_res.id
LEFT OUTER JOIN trfu_external_indicators ind ON ext_res.indicatorCode = ind.code
WHERE ext_entry.appointment_id = appointment_id AND ind.code IS NULL
GROUP BY ext_res.indicatorCode DESC;

END