package ru.efive.dao.sql.entity.enums;


public enum ConverterName {
    Blood_Group_Analysis_Converter("Конвертер для группы крови"),
    Default_Analysis_Converter("Конвертер стандартный"),
    Kell_Analysis_Converter("Конвертер для Kell антигена"),
    Rhesus_Factor_Analysis_Converter("Конвертер для резус-фактора"),
    K_Antigen_Immuno_Converter("Конвертер Kell антигена(Иммуносерология)"),
    D_Antigen_Immuno_Converter("Конвертер D антигена(Иммуносерология)"),
    CcEe_Antigens_Immuno_Converter("Конвертер C,c,E,e антигенов(Иммуносерология)");

    public final String alias;

    private ConverterName(String alias) {
        this.alias = alias;
    }

    public static ConverterName getConverterName(String alias){
        for(ConverterName converterName: ConverterName.values()){
            if(converterName.alias.equals(alias))
                return converterName;
        }
        return null;
    }
}
