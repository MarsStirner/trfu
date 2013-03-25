package ru.efive.medicine.niidg.trfu.dictionary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;

import ru.efive.dao.alfresco.AlfrescoNode;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

/**
 * Вложение
 * 
 * @author Alexey Vagizov
 */
public class Attachment extends AlfrescoNode {
	
	public Attachment() {
		super();
		setNamespace(ApplicationHelper.NAMESPACE);
		setNamespacePrefix(ApplicationHelper.NAMESPACE_PREFIX);
		setNodeType(ApplicationHelper.TYPE_CONTENT);
		List<String> path = new ArrayList<String>();
		path.add(ApplicationHelper.STORE_NAME);
		setPath(path);
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getCreated() {
		return created;
	}
	
	@Override
	public boolean setNodeProperties(NamedValue[] nodeProperties) {
		boolean result = false;
		try {
			for (NamedValue namedValue : nodeProperties) {
				if (namedValue.getName().endsWith(Constants.PROP_NAME)) {
					setDisplayName(namedValue.getValue());
				}
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public NamedValue[] getNodeProperties() {
		try {
			if (getDisplayName() == null || getDisplayName().equals("")) {
				setDisplayName(new String(("transfusion_attachment_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(getCreated())).getBytes(), "UTF-8"));
			}
			NamedValue namedValue[] = { Utils.createNamedValue(Constants.PROP_NAME, getDisplayName()) };
			return namedValue;
		}
		catch (Exception e) {
			e.printStackTrace();
			return new NamedValue[] {};
		}
	}
	
	private String displayName;
	private Date created;
	
	private static final long serialVersionUID = -2722742909627205138L;
}