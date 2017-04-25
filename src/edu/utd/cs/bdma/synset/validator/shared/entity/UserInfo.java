package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class UserInfo implements Serializable{

	@Id Long id;
	@Index private String emailAddress;
	private String encryptedPassword;
	private String firstName;
	private String secondName;
	private String language;
	private String country;
	private boolean isVerified;
	private boolean isAdmin;
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountry() {
		return country;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getSecondName() {
		return secondName;
	}
	
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public boolean isVerified() {
		return isVerified;
	}
	
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	
	public UserInfo(String emailAddress, String encryptedPassword, String firstName, String secondName, String language) {
		super();
		this.emailAddress = emailAddress;
		this.encryptedPassword = encryptedPassword;
		this.firstName = firstName;
		this.secondName = secondName;
		this.language = language;
		this.isAdmin = false;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public UserInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
}
