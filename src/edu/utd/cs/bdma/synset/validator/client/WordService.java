package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.FeedbackOnSynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetExample;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;

@RemoteServiceRelativePath("word")
public interface WordService extends RemoteService{
	
	public static final String LANG_ENGLISH = "en";
	
    public static final String LANG_SPANISH = "es";
    
    public static final String LANG_ARABIC = "ar";

	ArrayList<SynsetEntry> getSynsets(String word);
	
	ArrayList<SynsetWord> getWords(SynsetEntry entry, String langCode);
	
	ArrayList<SynsetEntryWithWords> getAll(String word, String cameoCode, String langCode);
	
	int addSynsetSelections(ArrayList<CameoSelectedSynset> selections, String cameoCode);
	
	int addFeedbackOnWords(ArrayList<FeedbackOnSynsetWord> feedbacks, String cameoCode);
	
	public ArrayList<SynsetEntryWithWords> addSynset(ArrayList<SynsetEntryWithWords> newEntries, String cameoCode, String word);
	
	public int addSynsetWord(ArrayList<SynsetWord> words);
	
	public ArrayList<String> getCountries(String langCode);
	
	public ArrayList<SynsetExample> addExamples(SynsetEntry entry, String[] examples);
	
	public String getCodingLanguage();
	
}
