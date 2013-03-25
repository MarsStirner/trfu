package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment;

public class KellAnalysisValueConverter extends AnalysisValueConverter {
    @Override
    public String convert(String external) {
        if(external.trim().equals("положительный")||external.trim().equals("Положительный"))
            return "Положительный";
        if(external.trim().equals("отрицательный")||external.trim().equals("Отрицательный"))
            return "Отрицательный";
        return external;
    }
}
