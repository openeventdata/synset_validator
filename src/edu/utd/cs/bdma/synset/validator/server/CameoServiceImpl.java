package edu.utd.cs.bdma.synset.validator.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.client.CameoService;
import edu.utd.cs.bdma.synset.validator.shared.CameoTreeNode;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntrySummery;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.RuleTranslationHistory;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.VerdictOnRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;

public class CameoServiceImpl extends RemoteServiceServlet implements CameoService {

	static ArrayList<CameoEntry> cameoList = new ArrayList<>();
	
	static ArrayList<String> summery = new ArrayList<>();
	
	static ArrayList<ArrayList<CameoTreeNode>> cameoTree = new ArrayList<ArrayList<CameoTreeNode>>();

	static {
		ObjectifyService.register(CameoEntry.class);
        ObjectifyService.register(CameoRule.class);
        ObjectifyService.register(CameoTranslatedRule.class);
        ObjectifyService.register(Word.class);
        ObjectifyService.register(VerdictOnRule.class);
        ObjectifyService.register(Submission.class);
        ObjectifyService.register(RuleTranslationHistory.class);
	}
	
	private static void loadRootCodes(){
		try{
		   BufferedReader br = new BufferedReader(new FileReader("core_cameo.txt"));
		   while (br.ready()){
			   String[] s = br.readLine().split("\t");
			   
		   }
		   
		}
		catch(Exception ex){
			
		}
	}
	
	@Override
	public CameoEntry getCameoInfo(String cameoCode) {
		// TODO Auto-generated method stub
		return ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
	}

	@Override
	public ArrayList<CameoRule> getCameoRules(String cameoCode, String word) {
		// TODO Auto-generated method stub
		CameoEntry entry = ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
		//Word textWord = ofy().load().type(Word.class).filter("text", word).first().now();
		addCameoEntryToSession(entry);
		clearSubmissionID();
		if (entry != null ){
			List<CameoRule> rules = ofy()
					                    .load()
					                     .type(CameoRule.class)
					                     .filter("cameoCode", entry.getCode())
					                     .filter("word", word)
					                     .list();
			ArrayList<CameoRule> filteredRules = new ArrayList<>();
			for (CameoRule rule: rules){
				if (rule.getSource().equalsIgnoreCase("CAMEO2")) filteredRules.add(rule);
				else if (rule.getLangCode().equalsIgnoreCase("en")) filteredRules.add(rule);
			}
			return filteredRules;
		}
		
		return null;
	}

	@Override
	public ArrayList<CameoTranslatedRule> getCameoTranslation(CameoRule rule, String langCode) {
		// TODO Auto-generated method stub
		String lCode = ((UserInfo)this.getThreadLocalRequest().getSession().getAttribute("user")).getLanguage();
		List<CameoTranslatedRule> list = ofy().load().type(CameoTranslatedRule.class).filter("ruleID", rule.getId()).filter("languageCode", lCode).list();
		
		log("Translated rules sent; "+list.size());
		return new ArrayList<>(list);
	}
	


	@Override
	public List<CameoEntrySummery> getCameoSummery() {
		// TODO Auto-generated method stub
		List<CameoEntry> list = ofy().load().type(CameoEntry.class).list();
		
		List<CameoEntrySummery> summeries = new ArrayList<>();
		for (CameoEntry e: list) {
			summeries.add(e.summerize());
			log(e.summery());
		}
		
		Collections.sort(summeries, new Comparator<CameoEntrySummery>(){
			public int compare(CameoEntrySummery o1, CameoEntrySummery o2) {
				return o1.getCode().compareTo(o2.getCode());
			};
		});
		
		return summeries;
	}

	@Override
	public ArrayList<String> getRelatedWords(String cameoCode) {
		// TODO Auto-generated method stub
		List<CameoRule> rules = ofy().load().type(CameoRule.class).filter("cameoCode",cameoCode).project("word").distinct(true).list();
		ArrayList<String> words = new ArrayList<>();
		for (CameoRule rule: rules){
			if (rule.getWord() != null)
			     words.add(rule.getWord().toLowerCase());
		}
		log("Number of words sent: "+words.size());
		return words;
	}

	@Override
	public int addVerdictsOnRule(ArrayList<VerdictOnRule> verdicts) {
		// TODO Auto-generated method stub
		Long submissionId = ((Submission)this.getThreadLocalRequest().getSession().getAttribute("submission")).getId();
		
		for (VerdictOnRule v: verdicts){
			//v.setIdUser(userID);
			v.setSubmissionId(submissionId);
		}
		return ofy().save().entities(verdicts).now().size();
	}
	
	private void clearSubmissionID(){
		Submission submission = (Submission) this.getThreadLocalRequest().getSession().getAttribute("submission");
		
		if (submission != null){
			this.getThreadLocalRequest().getSession().setAttribute("submission", null);
		}
	}
	
	private synchronized Submission getSubmissionId(){
		Submission submission = (Submission) this.getThreadLocalRequest().getSession().getAttribute("submission");
		Long userID = ((UserInfo)this.getThreadLocalRequest().getSession().getAttribute("user")).getId();
		if (submission == null){
			submission = new Submission();
			submission.setUserId(userID);
			ofy().save().entity(submission).now();
			this.getThreadLocalRequest().getSession().setAttribute("submission", submission);
		}
		
		return submission;
		
	}
	
	
	private void addCameoEntryToSession(CameoEntry entry){
		this.getThreadLocalRequest().getSession().setAttribute("cameo", entry);
	}
	
	@Override
	public String addRelatedWord(String newWord, String[] cameoRules, String cameoCode) {
		// TODO Auto-generated method stub
		//CameoEntry entry = ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
		UserInfo user = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user"));
		 
	    Long userID = user.getId();	
		String langCode = user.getLanguage();
	    newWord = newWord.toLowerCase();
		Word word = ofy().load().type(Word.class).filter("text", newWord).first().now();
		
		if (word == null){
			word = new Word(newWord);
			ofy().save().entity(word).now();
		}
		
		for (String cameoRule: cameoRules){
			CameoRule rule = new CameoRule(cameoRule, ""+userID);
			rule.setCameoCode(cameoCode);
			rule.setWord(newWord);
			ofy().save().entity(rule).now();
			
			CameoTranslatedRule tRule = new CameoTranslatedRule(langCode, "N/A", ""+userID, false);
			tRule.setRuleID(rule.getId());
			ofy().save().entity(tRule).now();
		}
		
		
		
		return newWord;
	}

	@Override
	public CameoRule addCameoRule(CameoRule rule) {
		// TODO Auto-generated method stub
		Long userID = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getId();
		List<CameoRule> rules = ofy().load().type(CameoRule.class)
				                         .filter("cameoCode", rule.getCameoCode())
				                         .filter("word", rule.getWord())
				                         .list();
		
		for (CameoRule r: rules){
			if (r.getRuleText().equals(rule.getRuleText())){
				return r;
			}
		}
		rule.setSource(""+userID);
		rule.setLangCode("en");
		ofy().save().entity(rule).now();
		return rule;
	}

	@Override
	public ArrayList<String> summeryTree() {
		// TODO Auto-generated method stub
		if (summery.size() == 0){
			try {
				BufferedReader br  = new BufferedReader(new FileReader("cameo_structure.txt"));
				while (br.ready()){
					summery.add(br.readLine());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return summery;
	}

	@Override
	public CameoTranslatedRule addTranslatedRule(CameoTranslatedRule rule) {
		// TODO Auto-generated method stub
		UserInfo user = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user"));
		 
	    Long userID = user.getId();	
		String langCode = user.getLanguage();
		
		CameoTranslatedRule tRule = ofy().load().type(CameoTranslatedRule.class)
				                   .filter("ruleID", rule.getId())
				                   .filter("source", ""+userID)
				                   .filter("languageCode", langCode).first().now();
		
		rule.setSource(""+userID);
		rule.setLanguageCode(langCode);
		ofy().save().entity(rule).now();
		tRule = rule;
	
		return tRule;
	}

	@Override
	public CameoTranslatedRule editTranslation(CameoTranslatedRule newRule) {
		// TODO Auto-generated method stub
		UserInfo user = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user"));
		CameoTranslatedRule tRule = ofy().load().type(CameoTranslatedRule.class).filterKey(Key.create(CameoTranslatedRule.class, newRule.getId())).first().now();
		tRule.setText(newRule.getText());
		ofy().save().entity(tRule).now();
		RuleTranslationHistory history = new RuleTranslationHistory();
		history.setRuleText(tRule.getText());
		history.setTime(new Date(System.currentTimeMillis()));
		history.setTranslatedRuleId(tRule.getId());
		history.setUserId(user.getId());
		ofy().save().entity(history).now();
		return tRule;
		
	}

	
	

}
