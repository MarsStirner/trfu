package ru.efive.medicine.niidg.trfu.context;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ApplicationContextHelper {
	
	public static void setApplicationContext(FileSystemXmlApplicationContext ctx) {
		ApplicationContextHelper.ctx = ctx;
    }

    public static FileSystemXmlApplicationContext getApplicationContext() {
        return ctx;
    }
	
	
	private static FileSystemXmlApplicationContext ctx;
}