package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

public class SynsetWordWithFeedback implements Serializable{
	
	
	private SynsetWord synsetWord;
	private FeedbackOnSynsetWord feedback;
	public SynsetWord getSynsetWord() {
		return synsetWord;
	}
	public void setSynsetWord(SynsetWord synsetWord) {
		this.synsetWord = synsetWord;
	}
	public FeedbackOnSynsetWord getFeedback() {
		return feedback;
	}
	public void setFeedback(FeedbackOnSynsetWord feedback) {
		this.feedback = feedback;
	}
	
	public SynsetWordWithFeedback() {
		// TODO Auto-generated constructor stub
	}
	

}
