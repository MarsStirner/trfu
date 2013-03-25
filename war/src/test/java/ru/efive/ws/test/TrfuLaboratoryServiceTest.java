package ru.efive.ws.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import ru.korusconsulting.laboratory.AnalysisResult;
import ru.korusconsulting.laboratory.TrfuLaboratoryIntegration_Service;

public class TrfuLaboratoryServiceTest {
	
	public TrfuLaboratoryServiceTest() {
		
	}
	
	//@Test
	public void runTest() throws Exception {

		TrfuLaboratoryIntegration_Service service = new TrfuLaboratoryIntegration_Service();
		List<AnalysisResult> list = new ArrayList<AnalysisResult>();
		AnalysisResult result;
        result = new AnalysisResult();
        result.setIndicatorCode("Ф0211");
        result.setResultValueText("Группа АВ (IV)");
        list.add(result);
        result = new AnalysisResult();
        result.setIndicatorCode("Ф0216");
        result.setResultValueText("отрицательный");
        list.add(result);

        result = new AnalysisResult();
        result.setIndicatorCode("Ф0212");
        result.setResultValueText("Группа АВ (IV)");
        list.add(result);
        result = new AnalysisResult();
        result.setIndicatorCode("Ф0217");
        result.setResultValueText("положительный");
        list.add(result);


        result = new AnalysisResult();
        result.setIndicatorCode("Ф0040");
        result.setResultValueText("отрицательный");
        list.add(result);

        result = new AnalysisResult();
        result.setIndicatorCode("Ф0200");
        result.setResultValueText("Фенотип С(-)с(+)Е(-)е(-)");
        list.add(result);

        System.out.println(service.getTrfuLaboratoryIntegration().setAnalysisResults(0, "00805", list, "", 0));

	}
	
}