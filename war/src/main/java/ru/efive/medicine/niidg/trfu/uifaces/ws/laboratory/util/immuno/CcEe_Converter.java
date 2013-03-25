package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CcEe_Converter extends ImmunoAnalysisValueConverter {
    @Override
    public String convert(String external, String antigen) {
        Pattern pattern = Pattern.compile(".*"+antigen+"\\(([+-])\\).*");
        Matcher matcher = pattern.matcher(external);
        if (matcher.matches())
            return matcher.group(1);
        return null;
    }
}
