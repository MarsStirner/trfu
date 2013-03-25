package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment;


public class RhesusFactorAnalysisValueConverter extends AnalysisValueConverter {
    @Override
    public String convert(String external) {
        if(external.trim().equals("положительный")||external.trim().equals("Положительный"))
            return "Положительный";
        if(external.trim().equals("отрицательный")||external.trim().equals("Отрицательный"))
            return "Отрицательный";
        if(external.trim().equals("Резус неопределен")||external.trim().equals("резус неопределен"))
            return "Не определен";
        return external;

    }
}
