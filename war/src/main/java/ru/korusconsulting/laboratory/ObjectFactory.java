
package ru.korusconsulting.laboratory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.korusconsulting.laboratory package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AnalysisResult_QNAME = new QName("http://www.korusconsulting.ru", "AnalysisResult");
    private final static QName _MicroOrganismResult_QNAME = new QName("http://www.korusconsulting.ru", "MicroOrganismResult");
    private final static QName _AntibioticSensitivity_QNAME = new QName("http://www.korusconsulting.ru", "AntibioticSensitivity");
    private final static QName _SetAnalysisResults_QNAME = new QName("http://www.korusconsulting.ru", "setAnalysisResults");
    private final static QName _SetAnalysisResultsResponse_QNAME = new QName("http://www.korusconsulting.ru", "setAnalysisResultsResponse");
    private final static QName _ImageValue_QNAME = new QName("http://www.korusconsulting.ru", "ImageValue");
    private final static QName _Exception_QNAME = new QName("http://www.korusconsulting.ru", "Exception");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.korusconsulting.laboratory
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.AnalysisResult }
     * 
     */
    public AnalysisResult createAnalysisResult() {
        return new AnalysisResult();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.SetAnalysisResultsResponse }
     * 
     */
    public SetAnalysisResultsResponse createSetAnalysisResultsResponse() {
        return new SetAnalysisResultsResponse();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.ImageValue }
     * 
     */
    public ImageValue createImageValue() {
        return new ImageValue();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.SetAnalysisResults }
     * 
     */
    public SetAnalysisResults createSetAnalysisResults() {
        return new SetAnalysisResults();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.AntibioticSensitivity }
     * 
     */
    public AntibioticSensitivity createAntibioticSensitivity() {
        return new AntibioticSensitivity();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.MicroOrganismResult }
     * 
     */
    public MicroOrganismResult createMicroOrganismResult() {
        return new MicroOrganismResult();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.AnalysisResult.ImageValues }
     * 
     */
    public AnalysisResult.ImageValues createAnalysisResultImageValues() {
        return new AnalysisResult.ImageValues();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.AnalysisResult.MicroValues }
     * 
     */
    public AnalysisResult.MicroValues createAnalysisResultMicroValues() {
        return new AnalysisResult.MicroValues();
    }

    /**
     * Create an instance of {@link ru.korusconsulting.laboratory.AnalysisResult.MicroSensitivityList }
     * 
     */
    public AnalysisResult.MicroSensitivityList createAnalysisResultMicroSensitivityList() {
        return new AnalysisResult.MicroSensitivityList();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ru.korusconsulting.laboratory.AnalysisResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.korusconsulting.ru", name = "AnalysisResult")
    public JAXBElement<AnalysisResult> createAnalysisResult(AnalysisResult value) {
        return new JAXBElement<AnalysisResult>(_AnalysisResult_QNAME, AnalysisResult.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ru.korusconsulting.laboratory.MicroOrganismResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.korusconsulting.ru", name = "MicroOrganismResult")
    public JAXBElement<MicroOrganismResult> createMicroOrganismResult(MicroOrganismResult value) {
        return new JAXBElement<MicroOrganismResult>(_MicroOrganismResult_QNAME, MicroOrganismResult.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ru.korusconsulting.laboratory.AntibioticSensitivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.korusconsulting.ru", name = "AntibioticSensitivity")
    public JAXBElement<AntibioticSensitivity> createAntibioticSensitivity(AntibioticSensitivity value) {
        return new JAXBElement<AntibioticSensitivity>(_AntibioticSensitivity_QNAME, AntibioticSensitivity.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ru.korusconsulting.laboratory.SetAnalysisResults }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.korusconsulting.ru", name = "setAnalysisResults")
    public JAXBElement<SetAnalysisResults> createSetAnalysisResults(SetAnalysisResults value) {
        return new JAXBElement<SetAnalysisResults>(_SetAnalysisResults_QNAME, SetAnalysisResults.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ru.korusconsulting.laboratory.SetAnalysisResultsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.korusconsulting.ru", name = "setAnalysisResultsResponse")
    public JAXBElement<SetAnalysisResultsResponse> createSetAnalysisResultsResponse(SetAnalysisResultsResponse value) {
        return new JAXBElement<SetAnalysisResultsResponse>(_SetAnalysisResultsResponse_QNAME, SetAnalysisResultsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ru.korusconsulting.laboratory.ImageValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.korusconsulting.ru", name = "ImageValue")
    public JAXBElement<ImageValue> createImageValue(ImageValue value) {
        return new JAXBElement<ImageValue>(_ImageValue_QNAME, ImageValue.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ru.korusconsulting.laboratory.Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.korusconsulting.ru", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

}
