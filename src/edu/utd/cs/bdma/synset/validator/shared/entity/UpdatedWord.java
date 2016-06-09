package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UpdatedWord implements Serializable{


	@Ignore public static final String DELETE = "D";
	@Ignore public static final String MODIFY = "M";
	
	@Id Long id;
	@Index Long idSynsetWord;
	@Index Long idUser;
	
	String updatedWord;
	String action;
	Date date;
	public UpdatedWord(String updatedWord, String action) {
		super();
		this.updatedWord = updatedWord;
		this.action = action;
		this.date = new Date();
	}
	
	public UpdatedWord() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getIdSynsetWord() {
		return idSynsetWord;
	}

	public void setIdSynsetWord(Long idSynsetWord) {
		this.idSynsetWord = idSynsetWord;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getId() {
		return id;
	}

	public String getUpdatedWord() {
		return updatedWord;
	}
	public void setUpdatedWord(String updatedWord) {
		this.updatedWord = updatedWord;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	
	
	
	
}
