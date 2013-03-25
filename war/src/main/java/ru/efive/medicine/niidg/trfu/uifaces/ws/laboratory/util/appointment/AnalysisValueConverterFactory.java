package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment;

import ru.efive.dao.sql.entity.enums.ConverterName;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;

public class AnalysisValueConverterFactory {
    public static AnalysisValueConverter getConverter(ExternalIndicator indicator){
        if(indicator.getConverterName()== ConverterName.Blood_Group_Analysis_Converter){
            return new BloodGroupAnalysisValueConverter();
        }
        if(indicator.getConverterName()==ConverterName.Rhesus_Factor_Analysis_Converter){
            return new RhesusFactorAnalysisValueConverter();
        }
        if(indicator.getConverterName()==ConverterName.Kell_Analysis_Converter){
            return new KellAnalysisValueConverter();
        }

        return new DefaultAnalysisValueConverter();
    }
}
