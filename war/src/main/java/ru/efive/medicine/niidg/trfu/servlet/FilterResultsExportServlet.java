package ru.efive.medicine.niidg.trfu.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.AbstractFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.FilterResultsExportGenerator;

public class FilterResultsExportServlet extends AbstractFacesServlet {
	private static final long serialVersionUID = 5222391413082191923L;

	@SuppressWarnings("rawtypes")
	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String beanName = request.getParameter("bean");
		String ext = request.getParameter("ext");
		String fileName = "export";
		File tempFile = File.createTempFile(fileName, null);
		tempFile.deleteOnExit();
		try {
			FacesContext fc = getFacesContext(request, response);
			Object bean = getManagedBean(beanName, fc);
			if (bean instanceof AbstractFilterableListHolderBean) {
				AbstractFilterableListHolderBean castedBean = (AbstractFilterableListHolderBean) bean;
				File logoFile = findLogoFile();
				FilterResultsExportGenerator generator = new FilterResultsExportGenerator();
				if ("docx".equals(ext)) {
					generator.generateDocx(tempFile, logoFile, castedBean);
				} else if ("xls".equals(ext)) {
					generator.generateXls(tempFile, logoFile, castedBean);
				} else if ("xlsx".equals(ext)) {
					generator.generateXlsx(tempFile, logoFile, castedBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO some processing?
		} finally {
			removeFacesContext();
		}
		downloadFile(response, tempFile, fileName + "." + ext,
				"application/octet-stream");
	}

	private File findLogoFile() {
		String relativePath = "/resources/images/logo.png";
		String absoluteDiskPath = getServletContext().getRealPath(relativePath);
		return new File(absoluteDiskPath);
	}

	public static void downloadFile(HttpServletResponse response,
			File fileContent, String fileName, String strMimeType)
			throws IOException {
		response.setContentType(strMimeType);
		response.setContentLength((int) fileContent.length());
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(fileContent);
			OutputStream responseOutputStream = response.getOutputStream();
			byte buffer[] = new byte[2048];
			for (int nRead = fileInputStream.read(buffer); nRead >= 0;) {
				responseOutputStream.write(buffer, 0, nRead);
				nRead = fileInputStream.read(buffer);
				responseOutputStream.flush();
			}
			responseOutputStream.flush();
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}
}