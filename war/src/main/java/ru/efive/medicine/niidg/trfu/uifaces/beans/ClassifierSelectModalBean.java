package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.List;

import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

public class ClassifierSelectModalBean extends ModalWindowHolderBean {
	
	public List<Classifier> getClassifierList() {
		return classifierList;
	}
	
	public void setClassifierList(List<Classifier> classifierList) {
		this.classifierList = classifierList;
	}
	
	public Classifier getClassifier() {
		return classifier;
	}
	
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}
	
	public void select(Classifier classifier) {
		this.classifier = classifier;
	}
	
	public boolean selected(Classifier classifier) {
		return this.classifier == null? false: this.classifier.equals(classifier);
	}
	
	@Override
	protected void doSave() {
		super.doSave();
	}
	
	@Override
	protected void doShow() {
		super.doShow();
	}
	
	@Override
    protected void doHide() {
		super.doHide();
    }
	
	
	private List<Classifier> classifierList = new ArrayList<>();
	private Classifier classifier;
	
	private static final long serialVersionUID = -3570593767875877766L;
}