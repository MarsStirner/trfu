package ru.efive.wf.core.util;

import org.junit.Test;

public class GroovyProcessorTest {

    @Test
    public void testTemplateProcessing() throws Exception {

        System.out.println("\ntemplate processing result:\n"+
                GroovyProcessor.processTemplate("blabla ${hello = 'hi'; if (1==1) return hello else return 'no'} fo fofo ${hello = 'hi'; if (1==0) return hello else return 'no';}", null));
    }
}
