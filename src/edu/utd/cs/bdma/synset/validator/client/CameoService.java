package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Key;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntrySummery;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.VerdictOnRule;

@RemoteServiceRelativePath("cameo")
public interface CameoService extends RemoteService{
	public CameoEntry getCameoInfo(String cameoCode);
	
	public ArrayList<CameoRule> getCameoRules(String cameoCode, String word);
	
	public ArrayList<CameoTranslatedRule> getCameoTranslation(CameoRule rule, String langCode);
	
	public List<CameoEntrySummery> getCameoSummery();
	
	public ArrayList<String> getRelatedWords(String cameoCode);
	
	public int addVerdictsOnRule(ArrayList<VerdictOnRule> verdicts);
	
	public String addRelatedWord(String newWord, String[] cameoRules, String cameoCode);
	
	public CameoRule addCameoRule(CameoRule rule);
	
	public CameoTranslatedRule addTranslatedRule(CameoTranslatedRule  rule);
	
	public ArrayList<String> summeryTree();
	
	public CameoTranslatedRule editTranslation(CameoTranslatedRule newRule);

}
