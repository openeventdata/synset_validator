package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntrySummery;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.VerdictOnRule;

public interface CameoServiceAsync {

	void getCameoInfo(String cameoCode, AsyncCallback<CameoEntry> callback);

	void getCameoRules(String cameoCode, String word, AsyncCallback<ArrayList<CameoRule>> callback);

	void getCameoTranslation(CameoRule rule, String langCode, AsyncCallback<ArrayList<CameoTranslatedRule>> callback);

	void getCameoSummery(AsyncCallback<List<CameoEntrySummery>> callback);

	void getRelatedWords(String cameoCode, AsyncCallback<ArrayList<String>> callback);

	void addVerdictsOnRule(ArrayList<VerdictOnRule> verdicts, AsyncCallback<Integer> callback);

	void addRelatedWord(String newWord, String[] cameoRules, String cameoCode, AsyncCallback<String> callback);

	void addCameoRule(CameoRule rule, AsyncCallback<CameoRule> callback);

	void summeryTree(AsyncCallback<ArrayList<String>> callback);

	void addTranslatedRule(CameoTranslatedRule rule, AsyncCallback<CameoTranslatedRule> callback);

	void editTranslation(CameoTranslatedRule newRule, AsyncCallback<CameoTranslatedRule> callback);


	


}
