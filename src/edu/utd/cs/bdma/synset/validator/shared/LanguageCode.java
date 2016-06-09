package edu.utd.cs.bdma.synset.validator.shared;

import java.io.Serializable;

public enum LanguageCode implements Serializable{
    ENGLISH("en"), 
    SPANISH("es"), 
    ARABIC("ar");
    
    private String code;

	public String getCode() {
		return code;
	}

	LanguageCode(String code){
		this.code = code;
	}
	
    
}
