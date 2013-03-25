package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno;

public class K_Converter extends ImmunoAnalysisValueConverter {
    @Override
    public String convert(String external, String antigen) {
        if(external.trim().equals("положительный")||external.trim().equals("Положительный"))
            return "+";
        if(external.trim().equals("отрицательный")||external.trim().equals("Отрицательный"))
            return "-";
        return null;
    }
}
