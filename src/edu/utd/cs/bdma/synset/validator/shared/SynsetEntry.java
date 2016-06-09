package edu.utd.cs.bdma.synset.validator.shared;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;

public class SynsetEntry implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String conceptID;
	private String concept;
	private String gloss;
	private Synset[] sets;
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getGloss() {
		return gloss;
	}
	public void setGloss(String gloss) {
		this.gloss = gloss;
	}
	public Synset[] getSets() {
		return sets;
	}
	public void setSets(Synset[] sets) {
		this.sets = sets;
	}
	public String getConceptID() {
		return conceptID;
	}
	public void setConceptID(String conceptID) {
		this.conceptID = conceptID;
	}
	public SynsetEntry(String concept, String gloss, Synset[] sets) {
		super();
		this.concept = concept;
		this.gloss = gloss;
		this.sets = sets;
	}
	
	public SynsetEntry() {
		// TODO Auto-generated constructor stub
	}
	
	public List<String> wordsInLanguage(LanguageCode langCode){
		for (Synset s: sets){
			if (s.getLang().equals(langCode.getCode())){
				return s.getWords();
			}
		}
		return null;
		
	}
	
	public List<String> wordsInLanguage(String langCode){
		GWT.log(langCode);
		GWT.log(""+(sets == null));
		
		for (Synset synset: sets){
			GWT.log(""+(synset==null));
			if (synset.getLang().equals(langCode)){
				return synset.getWords();
			}
		}
		return null;
		
	}
	

}
