package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SynsetEntryWithWords implements Serializable{
	
	SynsetEntry entry;
	
	ArrayList<SynsetWord> words;
	
	public SynsetEntryWithWords(SynsetEntry entry) {
		// TODO Auto-generated constructor stub
		this.entry = entry;
		words = new ArrayList<>();
	}
	
    public void addWord(SynsetWord word){
    	words.add(word);
    }
    
    public SynsetEntry getEntry() {
		return entry;
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
