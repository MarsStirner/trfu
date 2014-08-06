package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.print.*;
import java.io.*;
import java.lang.Exception;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.print.*;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.*;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.components.barcode4j.Code128Component;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;


import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.krysalis.barcode4j.*;
import org.krysalis.barcode4j.impl.code128.Code128;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ru.efive.dao.sql.entity.document.ReportTemplate;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.korusconsulting.laboratory.*;

@Named("reports")
@RequestScoped
public class ReportsManagmentBean implements Serializable {
    public void getHttpReportByXML() {
        System.out.println("Starting");
        Connection conn = null;
        try {
            FileSystemXmlApplicationContext context = indexManagement.getContext();
            BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
            System.out.println("Data source is " + dataSource);
            conn = dataSource.getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/customers.xml"));

            // Передаем resultSet в отчет
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), conn);

            JasperExportManager.exportReportToPdfFile(jasperPrint, "c:\\customers_report.pdf");
            //JasperExportManager.exportReportToHtmlFile(jasperPrint,
            //"C:\\customers_report.html");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            // Корректно закрываем соединение с базой
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done.");

    }

    public void printShortLabel() throws IOException, ClassNotFoundException, SQLException {
        FileSystemXmlApplicationContext context = indexManagement.getContext();
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        Connection conn = dataSource.getConnection();
        //FacesContext.getCurrentInstance().getExternalContext().get
        //Properties in_properties=new Properties();
        //in_properties.load(getClass().getClassLoader().getResourceAsStream("properties/print.properties"));

        //Check printer name
        String printer = "Xerox WorkCentre M165";//in_properties.getProperty("printer.name");

        //Get printer by checked name
        String in_printerName = printer;
        PrintService psZebra = null;
        String sPrinterName = null;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
            sPrinterName = ((PrinterName) attr).getValue();
            if (sPrinterName.equals(in_printerName)) {
                psZebra = services[i];
                break;
            }
        }
        if (psZebra == null) {
            System.out.println(in_printerName + " is not found.");
            return;
        }
        System.out.println("The printer you find is -> " + psZebra.getName());

		/* Get Data source */
        //--------------------------------------------------------------------------------------------------//
        Statement stmt = (Statement) conn.createStatement();
        ResultSet rs;
        //String request=in_properties.getProperty("jdbc.first_donor_by_rnumber").replaceFirst("%component_number%", args[1]).replaceFirst("%request_number%",args[0]);
        String request = "select d.firstName as firstName, d.middleName as middleName, d.lastName as lastName, d.number as d_number, r.number as r_number, r.created as created from trfu_donors d inner join trfu_examination_requests r on r.donor_id = d.id where r.number='00010'";
        rs = stmt.executeQuery(request);

        JasperReport report = null;
        JasperPrint print = null;
        try {
            //report=JasperCompileManager.compileReport("Barcode4JReport.xml");
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/Barcode4JReport.xml");
            //byte[]  buf = new byte[ inputStream.available() ];
            //inputStream.read( buf );
            //String contents = new String(buf);


            report = JasperCompileManager.compileReport(inputStream);
            //print=JasperFillManager.fillReport(report,new HashMap(), new JREmptyDataSource());
            print = JasperFillManager.fillReport(report, new HashMap(), new JRResultSetDataSource(rs));
            //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/Barcode4JReport.jasper");
            //print=JasperFillManager.fillReport(inputStream,new HashMap(), new JRResultSetDataSource(rs));
            JasperExportManager.exportReportToPdfFile(print, "c:/reports/db_big_report.pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }
        System.out.println("end");
        if (true) {
            return;
        }
        //--------------------------------------------------------------------------------------------------//
        //Configure page printing on found print

        DocPrintJob job = psZebra.createPrintJob();

        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        //System.out.println(Float.valueOf(args[1]).floatValue());
        MediaSizeName mediaSizeName = MediaSize.findMedia(30.0f, 20.0f, MediaPrintableArea.MM);
        //System.out.println(mediaSizeName);
        //MediaSizeName mediaSizeName = MediaSizeName.ISO_A4;
        printRequestAttributeSet.add(mediaSizeName);
        printRequestAttributeSet.add(new Copies(1));

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        /* We set the selected service and pass it as a paramenter */
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, psZebra);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, psZebra.getAttributes());
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

        try {
            exporter.exportReport();
            System.out.println("printed");
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println("not printed");
        } finally {
            // Корректно закрываем соединение с базой
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] args1 = {"1", "2"};
        //printReportByTemplateName({"1","2"});
    }

    public void hibernatePrintReportByRequestParams() throws IOException, ClassNotFoundException, SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> requestProperties = facesContext.getExternalContext().getRequestParameterMap();
        String in_reportName = requestProperties.get("reportName");
        String in_printerName = requestProperties.get("printerName");
        FileSystemXmlApplicationContext context = indexManagement.getContext();
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        Connection conn = dataSource.getConnection();

        if (in_printerName == null) {
            System.out.println("Wrong system configuration. Property reports.smallLabel.printerName is not set");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указано наименование принтера печати этикеток. Обратитесь в техническую поддержку", ""));
            return;
        }
        PrintService psZebra = null;
        String sPrinterName = null;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
            sPrinterName = ((PrinterName) attr).getValue();
            System.out.println(sPrinterName);
            if (sPrinterName.equals(in_printerName)) {
                psZebra = services[i];
                break;
            }
        }
        if (psZebra == null) {
            System.out.println(in_printerName + " is not found.");
            System.out.println("q" + in_printerName);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не найден принтер для печати этикеток", ""));
            return;
        } else {
            System.out.println("Found printer name is >> " + psZebra.getName());
            for(Attribute attr : psZebra.getAttributes().toArray()){
               System.out.println(" * "+attr.getName()+":"+ attr);
            }
        }
        Object count = propertiesHolder.getProperty("application", "reports.smallLabel.count");
        if (count == null) {
            System.out.println("Wrong system configuration. Property reports.smallLabel.count is not set");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не установлено количество печатаемых этикеток. Обратитесь в техническую поддержку", ""));
            return;
        }
        Session session = ((SessionFactory) indexManagement.getContext().getBean("sessionFactory")).openSession();
        final Map<String, Object> in_map = new HashMap<String, Object>();
        for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
            System.out.println("_" + entry.getKey());
            in_map.put("_" + entry.getKey(), entry.getValue());
            in_map.put(entry.getKey(), entry.getValue());
        }
        final List resultList = session.createQuery("select\n" +
                "request.number as r_number,\n" +
                "request.created as created,\n" +
                "request.donor.number as d_number,\n" +
                "request.donor.middleName as middleName,\n" +
                "request.donor.lastName as lastName,\n" +
                "request.donor.firstName as firstName\n" +
                "from\n" +
                "ExaminationRequest as request\n" +
                "where\n" +
                "request.number=\'" + in_map.get("_requestNumber") + "\'").list();
        if(resultList.isEmpty()) {
            return;
        }
        Object[] row = (Object[]) resultList.get(0);
        final String r_number = (String) row[0];
        final Date created = (Date)row[1];
        final String d_number = (String) row[2];
        final String middleName = (String) row[3];
        final String lastName = (String) row[4];
        final String firstName = (String) row[5];
        session.close();
        final StringBuilder fioStringBuilder = new StringBuilder(lastName);
        if(firstName != null && !firstName.isEmpty()){
            fioStringBuilder.append(' ').append(firstName.charAt(0)).append('.');
        }
        if(middleName != null && !middleName.isEmpty()){
            fioStringBuilder.append(' ').append(middleName.charAt(0)).append('.');
        }
        System.out.println("Print bufferedImage with Paper settings");
        final BufferedImage picture_240_160 = createLabel240_160(r_number, new SimpleDateFormat("dd.MM.yyyy").format(created), d_number, fioStringBuilder.toString());
        printImage240x160withPaper(picture_240_160, psZebra, count);
    }

    private void printImage240x160withPaper(BufferedImage print, PrintService ps, Object count) {
        try {
            final PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(ps);
            final PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new PrinterResolution(203, 203, PrinterResolution.DPI));
            aset.add(new JobName("Little Label printing from java", Locale.getDefault()));
            aset.add(PrintQuality.HIGH);
            aset.add(new Copies((Integer) count));
            PageFormat pf = job.defaultPage();
            dump(pf);
            Paper paper = pf.getPaper();
            //                        30X20mm
            double width = fromCMToPPI(3.0);
            double height = fromCMToPPI(2.0);
            paper.setSize(width, height);
            paper.setImageableArea(
                    0,
                    0,
                    width,
                    height);
            pf.setOrientation(PageFormat.PORTRAIT);
            pf.setPaper(paper);
            PageFormat validatePage = job.validatePage(pf);
            System.out.println("Valid- ");
            dump(validatePage);
            job.setPrintable(new ImagePrintable(print), pf);
            job.print(aset);
        }catch(Exception e){
            System.out.println("imagePrint failed:");
            e.printStackTrace();
        }
    }

    private BufferedImage createLabel240_160(String r_number, String date, String d_number, String fio) {
        final BufferedImage picture_240_160 = new BufferedImage(240, 160, BufferedImage.TYPE_INT_RGB);
        final Font mainFont = new Font("Tahoma", Font.PLAIN, 18);
        final Font edgeFont = new Font("Tahoma",Font.PLAIN, 15);
        AffineTransform rotationTransform = new AffineTransform();
        rotationTransform.rotate(Math.toRadians(-90));
        final Font rotatedFont = edgeFont.deriveFont(rotationTransform);
        Graphics2D g2d = (Graphics2D) picture_240_160.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,240,160);
        g2d.setColor(Color.BLACK);
        g2d.setFont(mainFont);
        g2d.drawString("Донация ".concat(r_number), 0, 120);
        g2d.drawString(fio,20,138);
        g2d.setFont(rotatedFont);
        g2d.drawString("№ ".concat(d_number), 20, 100);
        g2d.drawString(date,235,120);
        g2d.translate(20,5);
        Code128Bean bean = new Code128Bean();
        bean.doQuietZone(true);
        bean.setHeight(100);
        bean.setModuleWidth(2.5);
        bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        bean.generateBarcode(new Java2DCanvasProvider(g2d,0), r_number);
        return picture_240_160;
    }


    protected static double fromPPItoCM(double dpi) {
        return dpi / 72 / 0.393700787;
    }

    protected static double fromCMToPPI(double cm) {
        return toPPI(cm * 0.393700787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }




    public void hibernatePrintReportByRequestParams(Map<String, String> requestProperties) throws IOException, ClassNotFoundException, SQLException {
        String in_reportName = requestProperties.get("reportName");
        String in_printerName = requestProperties.get("printerName");
        FileSystemXmlApplicationContext context = indexManagement.getContext();
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        Connection conn = dataSource.getConnection();

        PrintService psZebra = null;
        String sPrinterName = null;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
            sPrinterName = ((PrinterName) attr).getValue();
            System.out.println(sPrinterName);
            if (sPrinterName.equals(in_printerName)) {
                psZebra = services[i];
                break;
            }
        }
        if (psZebra == null) {
            System.out.println(in_printerName + " is not found.");
            throw new IOException(in_printerName + " is not found.");
        } else {
            System.out.println("Found printer name is >> " + psZebra.getName());
        }

        JasperReport report = null;
        JasperPrint print = null;
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/" + in_reportName);
            report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> in_map = new HashMap<String, Object>();
            for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                System.out.println("_" + entry.getKey());
                in_map.put("_" + entry.getKey(), entry.getValue());
                in_map.put(entry.getKey(), entry.getValue());
            }

            Session session = ((SessionFactory) indexManagement.getContext().getBean("sessionFactory")).openSession();
            in_map.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
            print = JasperFillManager.fillReport(report, in_map);
            session.close();

        } catch (JRException e) {
            e.printStackTrace();
        }

        DocPrintJob job = psZebra.createPrintJob();
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        MediaSizeName mediaSizeName = MediaSize.findMedia(30.0F, 20.0F, MediaPrintableArea.MM);
        System.out.println(mediaSizeName);

        printRequestAttributeSet.add(new MediaPrintableArea(0, 0, 26, 37, MediaPrintableArea.MM));

        Object count = propertiesHolder.getProperty("application", "reports.smallLabel.count");
        if (count == null) {
            System.out.println("Wrong system configuration. Property reports.smallLabel.count is not set");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не установлено количество печатаемых этикеток. Обратитесь в техническую поддержку", ""));
        } else {
            printRequestAttributeSet.add(new Copies(Integer.parseInt(count.toString())));
        }

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, psZebra);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, psZebra.getAttributes());
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.OFFSET_X, new Integer(5));
        exporter.setParameter(JRPrintServiceExporterParameter.OFFSET_Y, new Integer(-5));

        try {
            exporter.exportReport();
            System.out.println("Printed success");
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println("not printed");
        } finally {
            // Корректно закрываем соединение с базой
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sqlPrintReportByRequestParams() throws IOException, ClassNotFoundException, SQLException {
        Map<String, String> requestProperties = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String in_reportName = requestProperties.get("reportName");
        FileSystemXmlApplicationContext context = indexManagement.getContext();
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        Connection conn = dataSource.getConnection();
		
		/* Get Data source */
        JasperReport report = null;
        JasperPrint print = null;
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/" + in_reportName);
            report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> in_map = new HashMap<String, Object>();
            //main_content_form:startDate:dateEditor
            for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                System.out.println("_" + entry.getKey());

                if (StringUtils.contains(entry.getKey(), "Date")) {
                    try {
                        System.out.println("-" + entry.getValue());
                        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(entry.getValue());
                        in_map.put("_" + entry.getKey(), date);
                        in_map.put(entry.getKey(), date);
                    } catch (ParseException e) {
                        System.out.println("Wrong date parameter");
                        in_map.put("_" + entry.getKey(), entry.getValue());
                        in_map.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    in_map.put("_" + entry.getKey(), entry.getValue());
                    in_map.put(entry.getKey(), entry.getValue());
                }
            }
            print = JasperFillManager.fillReport(report, in_map, conn);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            //Set reponse content type
            response.setContentType("application/pdf");
            //Export PDF file to browser window
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            // Корректно закрываем соединение с базой
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sqlPrintReportByRequestParams(Map<String, String> requestProperties) throws IOException, ClassNotFoundException, SQLException {
        String in_reportName = requestProperties.get("reportName");
        FileSystemXmlApplicationContext context = indexManagement.getContext();
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        Connection conn = dataSource.getConnection();
		
		/* Get Data source */
        JasperReport report = null;
        JasperPrint print = null;
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/" + in_reportName);
            report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> in_map = new HashMap<String, Object>();
            for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                System.out.println("_" + entry.getKey());

                if (StringUtils.contains(entry.getKey(), "Date")) {
                    try {
                        System.out.println("-" + entry.getValue());
                        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(entry.getValue());
                        in_map.put("_" + entry.getKey(), date);
                        in_map.put(entry.getKey(), date);
                    } catch (ParseException e) {
                        System.out.println("Wrong date parameter");
                        in_map.put("_" + entry.getKey(), entry.getValue());
                        in_map.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    in_map.put("_" + entry.getKey(), entry.getValue());
                    in_map.put(entry.getKey(), entry.getValue());
                }
            }
            print = JasperFillManager.fillReport(report, in_map, conn);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            //Set reponse content type
            response.setContentType("application/pdf");
            //Export PDF file to browser window
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exporter.exportReport();
            //Отдать пдфку на просмотр
            response.getOutputStream().flush();
            response.getOutputStream().close();

            final File imageFile = exportToImage(print, requestProperties);
            if(imageFile != null){
                if(storePictureLinkToDatabase(imageFile, requestProperties)){
                    System.out.println("Successful store labelPath");
                } else {
                    System.out.println("Failed to store labelPath");
                }
            }
        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            // Корректно закрываем соединение с базой
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean storePictureLinkToDatabase(final File file, final Map<String, String> requestProperties) {
        final String docType = requestProperties.get("docType");
        if(docType == null || docType.isEmpty()){
            System.out.println("Cannot store PictureImage to Database cause: Undefined docType");
            return false;
        }
        if("BloodComponent".equalsIgnoreCase(docType)){
            return storePictureLinkToBloodComponent(file, requestProperties.get("docId"));
        } // .... other store to database calls
        System.out.println("Unknown docType");
        return false;
    }

    private boolean storePictureLinkToBloodComponent(final File file, final String id) {
        final BloodComponentDAOImpl bloodComponentDAO = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
        final BloodComponent bloodComponent = bloodComponentDAO.get(Integer.parseInt(id));
        bloodComponent.setBigLabelPath(file.getPath());
        return bloodComponentDAO.update(bloodComponent).getBigLabelPath().equals(file.getPath());
    }

    private File exportToImage(final JasperPrint print, final Map<String, String> requestProperties) {
        final float scale = getPictureScaleProperty();
        final String extension = getPictureExtension();
        final File pictureFile = getPictureFile(extension, requestProperties);
        if(pictureFile != null) {
            try {
                BufferedImage picture = new BufferedImage((int) (print.getPageWidth() * scale) + 1, (int) (print.getPageHeight() * scale) + 1, BufferedImage.TYPE_INT_RGB);
                JRGraphics2DExporter pictureExporter = new JRGraphics2DExporter();
                pictureExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                pictureExporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, picture.createGraphics());
                pictureExporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, scale);
                pictureExporter.exportReport();
                if (ImageIO.write(picture, extension, pictureFile)) {
                    System.out.println("Picture \'" + pictureFile.getAbsolutePath() + "\' Scale:"+scale+" Size: " + picture.getWidth() + 'x' + picture.getHeight() + " and use " + pictureFile.length() / 1024 + "Kb.");
                } else {
                    System.out.println("Export ot image failed without exception");
                }
            } catch (Exception e) {
                System.out.println("Export ot image failed");
                e.printStackTrace();
                return null;
            }
        }
        return pictureFile;
    }

    private File getPictureFile(final String extension, final Map<String, String> requestProperties) {
        String storagePath;
        try {
            storagePath = (String) propertiesHolder.getProperty("application", "reports.storage.path");
        } catch (Exception e) {
            System.out.println("Picture storage path property is empty or invalid use relative path \'..\\pictures\\'");
            storagePath = "\\..\\pictures\\";
        }
        String donorId;
        if (requestProperties.containsKey("donorId")) {
            donorId = requestProperties.get("donorId");
        } else {
            donorId = "UNDEFINED";
        }
        String fileName;
        if (requestProperties.containsKey("docId")) {
            fileName = requestProperties.get("docId");
        } else {
            fileName = "undefined_".concat(UUID.randomUUID().toString());
        }
        StringBuilder sb = new StringBuilder(storagePath);
        sb.append('\\').append(donorId)
                .append('\\').append(fileName)
                .append('.').append(extension);
        final File result = new File(sb.toString());
        result.mkdirs();
        return result;
    }

    private String getPictureExtension() {
        try {
            Object extensionProperty = propertiesHolder.getProperty("application", "reports.storage.extension");
            return (String) extensionProperty;
        } catch (Exception e) {
            System.out.println("Extension property is not defined or incorrect. Use default=\'png\'");
            return "png";
        }
    }

    private float getPictureScaleProperty() {
        try {
            Object scaleProperty = propertiesHolder.getProperty("application", "reports.bigLabelPictureScale");
            float scale = scaleProperty instanceof Number ? ((Number) scaleProperty).floatValue() : Float.parseFloat(scaleProperty.toString());
            if (scale > 10.0f) {
                System.out.println("ScaleProperty is greater then 10.0f. Use 10.0f.");
                return 10.0f;
            } else if (scale < 0.0f) {
                System.out.println("ScaleProperty is lesser then 0.0f. Use 1.0f.");
                return 1.0f;
            }
            return scale;
        } catch (Exception e) {
            System.out.println("Scale property is not defined or incorrect. Use default=2.0f");
            return 2.0f;
        }
    }

    public void sqlPrintReportByRequestParams(ReportTemplate reportTemplate) throws IOException, ClassNotFoundException, SQLException {
        FileSystemXmlApplicationContext context = indexManagement.getContext();
        Map<String, Object> requestProperties = reportTemplate.getProperties();
        String in_reportName = requestProperties.get("reportName").toString();
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        Connection conn = dataSource.getConnection();
		
		/* Get Data source */
        JasperReport report = null;
        JasperPrint print = null;
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/" + in_reportName);
            report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> in_map = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : requestProperties.entrySet()) {
                System.out.println("_" + entry.getKey() + ": " + entry.getValue());
                in_map.put("_" + entry.getKey(), entry.getValue());
                in_map.put(entry.getKey(), entry.getValue());
            }
            print = JasperFillManager.fillReport(report, in_map, conn);

            //JasperExportManager.exportReportToPdfFile(print,"c:/reports/db_big_report.pdf");

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            //response.reset();
            //response.resetBuffer();
            //Set reponse content type
            response.setContentType("application/pdf");
            //Export PDF file to browser window
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exporter.exportReport();
            //response.getOutputStream().flush();
            //response.getOutputStream().close();
        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            // Корректно закрываем соединение с базой
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sqlPrintReportByRequestParams(ReportTemplate reportTemplate, Map<String, Object> genericProperties) throws IOException, ClassNotFoundException, SQLException {
        FileSystemXmlApplicationContext context = indexManagement.getContext();
        Map<String, Object> requestProperties = reportTemplate.getProperties();

        for (Map.Entry<String, Object> entry : genericProperties.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getKey()) && entry.getValue() != null && StringUtils.isNotEmpty(entry.getValue().toString())) {
                requestProperties.put(entry.getKey(), entry.getValue());
            }
        }

        String in_reportName = requestProperties.get("reportName").toString();
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        Connection conn = dataSource.getConnection();
		
		/* Get Data source */
        JasperReport report = null;
        JasperPrint print = null;
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/" + in_reportName);
            report = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> in_map = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : requestProperties.entrySet()) {
                System.out.println("_" + entry.getKey() + ": " + entry.getValue());
                in_map.put("_" + entry.getKey(), entry.getValue());
                in_map.put(entry.getKey(), entry.getValue());
            }
            print = JasperFillManager.fillReport(report, in_map, conn);

            //JasperExportManager.exportReportToPdfFile(print,"c:/reports/db_big_report.pdf");

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            //response.reset();
            //response.resetBuffer();
            //Set reponse content type
            response.setContentType("application/pdf");
            //Export PDF file to browser window
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exporter.exportReport();
            //response.getOutputStream().flush();
            //response.getOutputStream().close();
        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            // Корректно закрываем соединение с базой
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Inject
    @Named("indexManagement")
    private transient IndexManagementBean indexManagement;

    @Inject
    @Named("propertiesHolder")
    private transient ApplicationPropertiesHolder propertiesHolder;

    @Inject @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement = new SessionManagementBean();



    protected String dump(Paper paper) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(paper.getWidth()).append("x").append(paper.getHeight())
                .append("/").append(paper.getImageableX()).append("x").
                append(paper.getImageableY()).append(" - ").append(paper
                .getImageableWidth()).append("x").append(paper.getImageableHeight());
        return sb.toString();
    }

    protected void dump(PageFormat pf) {
        Paper paper = pf.getPaper();
        System.out.println(dump(paper));
    }

    private class ImagePrintable implements Printable {
        private final BufferedImage image;

        public ImagePrintable(BufferedImage print) {
            this.image = print;
        }
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
           if(pageIndex >= 1){
               return NO_SUCH_PAGE;
           }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            if(this.image != null){
                Double xScaleFactor = 1.0d;
                Double yScaleFactor = 1.0d;

                if (this.image.getWidth() > pageFormat.getImageableWidth()) {
                    xScaleFactor = pageFormat.getImageableWidth() / (double) this.image.getWidth();
                } else if (this.image.getWidth() < pageFormat.getImageableWidth()) {
                    xScaleFactor = (double) this.image.getWidth() / pageFormat.getImageableWidth();
                }

                if (this.image.getHeight() > pageFormat.getImageableHeight()) {
                    yScaleFactor = pageFormat.getImageableHeight() / (double) this.image.getHeight();
                } else if (this.image.getHeight() < pageFormat.getImageableHeight()) {
                    yScaleFactor = (double) this.image.getHeight() / pageFormat.getImageableHeight();
                }

                printImage(g2d, this.image, xScaleFactor, yScaleFactor);

                return Printable.PAGE_EXISTS;
            } else {
                return Printable.NO_SUCH_PAGE;
            }
        }

        public void printImage(Graphics2D g2d, RenderedImage image, double xScale, double yScale) {
            if ((image == null) || (g2d == null)) {
                return;
            }
            AffineTransform at = new AffineTransform();
            g2d.scale(xScale, yScale);
            g2d.drawRenderedImage(image, at);
        }
    }
}