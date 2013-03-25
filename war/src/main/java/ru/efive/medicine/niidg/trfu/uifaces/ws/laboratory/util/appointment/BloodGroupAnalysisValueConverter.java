package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BloodGroupAnalysisValueConverter extends AnalysisValueConverter {
    private static Pattern mainPattern = Pattern.compile("[Гг]руппа (.*)");
    private static Pattern groupPattern = Pattern.compile(".*\\([IV]+\\)");


    @Override
    public String convert(String external) {
        Matcher matcher = mainPattern.matcher(external.trim());

        if (matcher.matches()){
            String group = matcher.group(1);
            if (group.equals("неопределена")) {
                return "Не определена";
            }
            if (groupPattern.matcher(group).matches())
                return group;
        }
        logger.warn("Cant convert result:" + external);
        return external;
    }
}
