package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SynsetEntryWithWords implements Serializable{
	
	SynsetEntry entry;
	ArrayList<SynsetExample> examples;
	
	ArrayList<SynsetWord> words;
	
	private boolean readOnly;
	
	public SynsetEntryWithWords(SynsetEntry entry) {
		// TODO Auto-generated constructor stub
		this.entry = entry;
		words = new ArrayList<>();
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	boolean getReadOnly(){
		return readOnly;
	}
	
    public void addWord(SynsetWord word){
    	words.add(word);
    }
    
    public SynsetEntry getEntry() {
		return entry;
	}
    
    public void setExamples(ArrayList<SynsetExample> examples) {
		this.examples = examples;
	}
    
    public ArrayList<SynsetExample> getExamples() {
		return examples;
	}
    
    public ArrayList<SynsetWord> getWords() {
		return words;
	}
    
    public void addAllWords(ArrayList<SynsetWord> list){
    	words.addAll(list);
    }
    
    public void addWord(String word, String langCode){
    	words.add(new SynsetWord(word, langCode));
    }
    
    public SynsetEntryWithWords() {
		// TODO Auto-generated constructor stub
	}
    
    public void removeWords(){
    	words.clear();
    }

}
