package edu.utd.cs.bdma.synset.validator.shared;

import java.io.Serializable;

public class CameoTreeNode implements Serializable{

	String code;
	String desc;
	boolean clickable;
	
	
	public CameoTreeNode(String code, String desc, boolean clickable) {
		super();
		this.code = code;
		this.desc = desc;
		this.clickable = clickable;
	}
	
	public CameoTreeNode() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isClickable() {
		return clickable;
	}
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
	
	
	
}
