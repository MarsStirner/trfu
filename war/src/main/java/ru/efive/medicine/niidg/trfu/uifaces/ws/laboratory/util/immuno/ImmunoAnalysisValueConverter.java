package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ImmunoAnalysisValueConverter {
    protected static final Logger logger = LoggerFactory.getLogger(ImmunoAnalysisValueConverter.class);
    public abstract String convert(String external, String antigen);
}
