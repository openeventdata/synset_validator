package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class Word implements Serializable{

	@Id Long id;
    
	@Index String text;
	
	public String getText() {
		return text;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Word() {
		// TODO Auto-generated constructor stub
	}
	
	public Word(String text){
		this.text = text;
	}
	
	
	
}
