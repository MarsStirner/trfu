package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment;


import org.apache.log4j.Logger;

public abstract class AnalysisValueConverter {
    protected static final Logger logger = Logger.getLogger(AnalysisValueConverter.class);
    public abstract String convert(String external);
}
