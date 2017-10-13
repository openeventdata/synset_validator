package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.editor.client.Editor.Ignore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ForumMessage implements Serializable{
	
	@Id private Long id;
	@Index private Long cameoID;
	@Index private Long wordID;
	@Index private Long userID;
	
	
	@Ignore private String fullName;
	private String message;
	private Date date;
	
	
	public ForumMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCameoID() {
		return cameoID;
	}
	
	public void setCameoID(Long cameoID) {
		this.cameoID = cameoID;
	}
	
	public Long getWordID() {
		return wordID;
	}
	
	public void setWordID(Long wordID) {
		this.wordID = wordID;
	}
	
	public Long getUserID() {
		return userID;
	}
	
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	
	
	

}
