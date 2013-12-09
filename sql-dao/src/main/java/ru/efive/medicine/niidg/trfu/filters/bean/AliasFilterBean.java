package ru.efive.medicine.niidg.trfu.filters.bean;

public class AliasFilterBean {
	private String associationPath;
	private String alias;
	private int joinType;
	public String getAssociationPath() {
		return associationPath;
	}
	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getJoinType() {
		return joinType;
	}
	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}
	
	private AliasFilterBean() {
		super();
	}
	public static AliasFilterBean initAlias(String associationPath, String alias, int joinType) {
		AliasFilterBean res = new AliasFilterBean();
		res.setAlias(alias);
		res.setAssociationPath(associationPath);
		res.setJoinType(joinType);
		return res;
	}
	public static AliasFilterBean initAlias(String associationPath, int joinType) {
		AliasFilterBean res = new AliasFilterBean();
		res.setAlias(associationPath);
		res.setAssociationPath(associationPath);
		res.setJoinType(joinType);
		return res;
	}

}
