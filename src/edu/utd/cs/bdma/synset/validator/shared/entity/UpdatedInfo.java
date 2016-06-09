package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdatedInfo implements Serializable{
	
	private ArrayList<FeedbackOnSynsetWord> feedbackOnSynsetWords = new ArrayList<>();
	
	private ArrayList<CameoSelectedSynset> cameoSelectedSynsets= new ArrayList<>();
	
	private ArrayList<VerdictOnRule> verdictsOnRules= new ArrayList<>();
	
	private ArrayList<SynsetEntryWithWords> synsetEntryWithWords= new ArrayList<>();
	
	private ArrayList<SynsetWordWithFeedback> newWords= new ArrayList<>();
	
	private HashMap<Long, FeedbackOnSynsetWord> feedbackSWNewEntry = new HashMap<>();

	public ArrayList<FeedbackOnSynsetWord> getFeedbackOnSynsetWords() {
		return feedbackOnSynsetWords;
	}
	
	public void setFeedbackSWNewEntry(HashMap<Long, FeedbackOnSynsetWord> feedbackSWNewEntry) {
		this.feedbackSWNewEntry = feedbackSWNewEntry;
	}
	
	public HashMap<Long, FeedbackOnSynsetWord> getFeedbackSWNewEntry() {
		return feedbackSWNewEntry;
	}

	public void setFeedbackOnSynsetWords(ArrayList<FeedbackOnSynsetWord> feedbackOnSynsetWords) {
		this.feedbackOnSynsetWords = feedbackOnSynsetWords;
	}

	public ArrayList<CameoSelectedSynset> getCameoSelectedSynsets() {
		return cameoSelectedSynsets;
	}

	public void setCameoSelectedSynsets(ArrayList<CameoSelectedSynset> cameoSelectedSynsets) {
		this.cameoSelectedSynsets = cameoSelectedSynsets;
	}

	public ArrayList<VerdictOnRule> getVerdictsOnRules() {
		return verdictsOnRules;
	}

	public void setVerdictsOnRules(ArrayList<VerdictOnRule> verdictsOnRules) {
		this.verdictsOnRules = verdictsOnRules;
	}

	public ArrayList<SynsetEntryWithWords> getSynsetEntryWithWords() {
		return synsetEntryWithWords;
	}

	public void setSynsetEntryWithWords(ArrayList<SynsetEntryWithWords> synsetEntryWithWords) {
		this.synsetEntryWithWords = synsetEntryWithWords;
	}

	public ArrayList<SynsetWordWithFeedback> getNewWords() {
		return newWords;
	}

	public void setNewWords(ArrayList<SynsetWordWithFeedback> arrayList) {
		this.newWords = arrayList;
	}
	
	

}
