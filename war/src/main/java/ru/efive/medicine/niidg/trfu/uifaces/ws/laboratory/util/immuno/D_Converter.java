package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno;

/**
 * Created with IntelliJ IDEA.
 * User: Valiev
 * Date: 27.09.12
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public class D_Converter extends ImmunoAnalysisValueConverter {
    @Override
    public String convert(String external, String antigen) {
        if(external.trim().equals("положительный")||external.trim().equals("Положительный"))
            return "+";
        if(external.trim().equals("отрицательный")||external.trim().equals("Отрицательный"))
            return "-";
        return null;
    }
}
