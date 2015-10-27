package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AnalysisValueConverter {
    protected static final Logger logger = LoggerFactory.getLogger(AnalysisValueConverter.class);
    public abstract String convert(String external);
}
