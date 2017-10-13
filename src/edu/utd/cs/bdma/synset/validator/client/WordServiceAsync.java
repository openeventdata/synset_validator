package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.FeedbackOnSynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetExample;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;

public interface WordServiceAsync {

	void getSynsets(String word, AsyncCallback<ArrayList<SynsetEntry>> callback);

	void getWords(SynsetEntry entry, String langCode, AsyncCallback<ArrayList<SynsetWord>> callback);

	void getAll(String word, String cameoCode, String langCode, AsyncCallback<ArrayList<SynsetEntryWithWords>> callback);

	void addSynsetSelections(ArrayList<CameoSelectedSynset> selections, String cameoCode,
			AsyncCallback<Integer> callback);

	void addFeedbackOnWords(ArrayList<FeedbackOnSynsetWord> feedbacks, String cameoCode,
			AsyncCallback<Integer> callback);

	void addSynset(ArrayList<SynsetEntryWithWords> newEntries, String cameoCode, String word,
			AsyncCallback<ArrayList<SynsetEntryWithWords>> callback);

	void addSynsetWord(ArrayList<SynsetWord> words, AsyncCallback<Integer> callback);

	void getCountries(String langCode, AsyncCallback<ArrayList<String>> callback);

	void addExamples(SynsetEntry entry, String[] examples, AsyncCallback<ArrayList<SynsetExample>> callback);

	void getCodingLanguage(AsyncCallback<String> callback);

	
	
}
