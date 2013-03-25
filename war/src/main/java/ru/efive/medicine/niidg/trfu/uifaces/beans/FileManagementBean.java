package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.io.InputStream;
import java.util.Calendar;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.alfresco.webservice.util.ContentUtils;

import ru.efive.dao.alfresco.AlfrescoDAO;
import ru.efive.medicine.niidg.trfu.dictionary.Attachment;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.filter.UploadHandler;
import ru.efive.uifaces.filter.UploadInfo;

@Named("fileManagement")
@SessionScoped
public class FileManagementBean implements java.io.Serializable, UploadHandler {
	
	// uploading

	public UploadHandler getUploadHandler() {
		return this;
	}

	@Override
	public void handleUpload(UploadInfo uploadInfo) {
		details = new FileUploadDetails();
		if (uploadInfo.getFileName() == null || uploadInfo.getFileName().isEmpty()) {
			System.out.println("file name is " + (uploadInfo.getFileName() == null? "null": "empty"));
			return;
		}
		try {
			byte[] byteArray = ContentUtils.convertToByteArray(uploadInfo.getData());
			Attachment attachment = new Attachment();
			attachment.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
			attachment.setDisplayName(new String(uploadInfo.getFileName().getBytes("UTF-8")));			
			uploadInfo.getData().close();
			details.setAttachment(attachment);			
			details.setByteArray(byteArray);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public class FileUploadDetails {
		
		public byte[] getByteArray() {
			return byteArray;
		}
		
		public void setByteArray(byte[] byteArray) {
			this.byteArray = byteArray;
		}
		
		public String getAsString() {
			return new String(byteArray);
		}
		
		public Attachment getAttachment() {
			return attachment;
		}
		
		public void setAttachment(Attachment attachment) {
			this.attachment = attachment;
		}
		
		private byte[] byteArray;
		private Attachment attachment;
	}
	
	public FileUploadDetails getDetails() {
		return details;
	}
	
	private FileUploadDetails details;
	
	// FILE OPERATIONS
	
	public synchronized Attachment getFileById(int id) {
		Attachment result = null;
		AlfrescoDAO<Attachment> dao = null;
		try {
			dao = sessionManagementBean.getAlfrescoDAO(Attachment.class);
			result = dao.getDataById(Integer.toString(id));
		}
		catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		finally {
			if (dao != null) dao.disconnect();
		}
		return result;
	}
	
	public synchronized Attachment getFileById(String id) {
		Attachment result = null;
		AlfrescoDAO<Attachment> dao = null;
		try {
			dao = sessionManagementBean.getAlfrescoDAO(Attachment.class);
			result = dao.getDataById(id);
		}
		catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		finally {
			if (dao != null) dao.disconnect();
		}
		return result;
	}
	
	public synchronized boolean createFile(Attachment file, byte[] bytes) {
		boolean result = false;
		AlfrescoDAO<Attachment> dao = null;
		try {
			dao = sessionManagementBean.getAlfrescoDAO(Attachment.class);
			result = dao.createContent(file, bytes);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (dao != null) dao.disconnect();
		}
		return result;
	}
	
	public synchronized boolean createFile(Attachment file, InputStream inputStream) {
		boolean result = false;
		AlfrescoDAO<Attachment> dao = null;
		try {
			dao = sessionManagementBean.getAlfrescoDAO(Attachment.class);
			result = dao.createContent(file, inputStream);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (dao != null) dao.disconnect();
		}
		return result;
	}
	
	public synchronized boolean deleteFile(Attachment file) {
		boolean result = false;
		AlfrescoDAO<Attachment> dao = null;
		try {
			dao = sessionManagementBean.getAlfrescoDAO(Attachment.class);
			result = dao.deleteData(file);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (dao != null) dao.disconnect();
		}
		return result;
	}
	
	public synchronized byte[] downloadFile(String id) {
		byte [] result = {};
		AlfrescoDAO<Attachment> dao = null;
		if (id != null && !id.equals("")) {
			try {
				dao = sessionManagementBean.getAlfrescoDAO(Attachment.class);
				Attachment attachment = dao.getDataById(id);
				result = dao.getContent(attachment);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (dao != null) dao.disconnect();
			}
		}
		return result;
	}
	
	
	@Inject @Named("sessionManagement")
    private transient SessionManagementBean sessionManagementBean;
	
	private static final long serialVersionUID = -4470091321891731494L;
}