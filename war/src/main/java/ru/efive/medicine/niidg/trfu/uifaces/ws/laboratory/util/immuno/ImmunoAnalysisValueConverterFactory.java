package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno;


import ru.efive.dao.sql.entity.enums.ConverterName;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;

public class ImmunoAnalysisValueConverterFactory {
    public static ImmunoAnalysisValueConverter getConverter(ExternalIndicator indicator) {
        if(indicator.getConverterName()== ConverterName.D_Antigen_Immuno_Converter){
            return new D_Converter();
        }
        if(indicator.getConverterName()==ConverterName.K_Antigen_Immuno_Converter){
            return new K_Converter();
        }
        if(indicator.getConverterName()==ConverterName.CcEe_Antigens_Immuno_Converter  ){
            return new CcEe_Converter();
        }
        return new ImmunoAnalysisValueConverter() {
            @Override
            public String convert(String external, String antigen) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

}
