package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno;


import org.apache.log4j.Logger;

public abstract class ImmunoAnalysisValueConverter {
    protected static final Logger logger = Logger.getLogger(ImmunoAnalysisValueConverter.class);
    public abstract String convert(String external, String antigen);
}
