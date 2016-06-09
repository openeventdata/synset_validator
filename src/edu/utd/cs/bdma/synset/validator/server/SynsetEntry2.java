package edu.utd.cs.bdma.synset.validator.server;

import java.io.Serializable;

public class SynsetEntry2 implements Serializable{
	
	
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
	public SynsetEntry2(String concept, String gloss, Synset[] sets) {
		super();
		this.concept = concept;
		this.gloss = gloss;
		this.sets = sets;
	}
	
	public SynsetEntry2() {
		// TODO Auto-generated constructor stub
	}
	public String getConceptID() {
		return conceptID;
	}
	public void setConceptID(String conceptID) {
		this.conceptID = conceptID;
	}
	
	
		

}
