package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.context.NoPropertyStoragePathException;

@Singleton
@Startup
@Named("indexManagement")
@ApplicationScoped
public class IndexManagementBean implements Serializable {
    private String storagePath;

	@PostConstruct
	public void initializeIndex() throws NoPropertyStoragePathException {
        storagePath = System.getProperty("efive.trfu.home");
        if (StringUtils.isEmpty(storagePath)) {
            throw new NoPropertyStoragePathException();
        }
        if(!storagePath.endsWith("/")){
            storagePath+="/";
        }
        if (storagePath.startsWith("/")){
            storagePath = "file:"+storagePath;
        } else {
            storagePath = "file:/"+storagePath;
        }
        context = new FileSystemXmlApplicationContext(storagePath+"applicationContext.xml");
        ApplicationContextHelper.setApplicationContext(context);
	}
	
	@PreDestroy
	public void dispose() {
		context.close();
	}

    public void reset() {
        ApplicationContextHelper.getApplicationContext().close();
        ApplicationContextHelper.setApplicationContext(new FileSystemXmlApplicationContext(storagePath+"applicationContext.xml"));
    }
	
	public FileSystemXmlApplicationContext getContext() {
		return context;
	}
	
	private FileSystemXmlApplicationContext context;
	
	private static final long serialVersionUID = 2489903807452724602L;
}