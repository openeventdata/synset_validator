package edu.utd.cs.bdma.synset.validator.shared;
import java.io.Serializable;
import java.util.ArrayList;

public class Synset implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String lang;
	private ArrayList<String> words;
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public ArrayList<String> getWords() {
		return words;
	}
	public void setWords(ArrayList<String> words) {
		this.words = words;
	}
	public Synset(String lang, ArrayList<String> words) {
		super();
		this.lang = lang;
		this.words = words;
	}
	
	public Synset() {
		// TODO Auto-generated constructor stub
	}
	
}
