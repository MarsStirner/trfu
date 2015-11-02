package ru.efive.medicine.niidg.trfu.context;

import org.springframework.context.ApplicationContext;

public class ApplicationContextHelper {
	
	public static void setApplicationContext(ApplicationContext ctx) {
		ApplicationContextHelper.ctx = ctx;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
	
	
	private static ApplicationContext ctx;
}