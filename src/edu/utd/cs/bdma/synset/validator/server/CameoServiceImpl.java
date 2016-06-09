package edu.utd.cs.bdma.synset.validator.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.VerdictOnRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;

public class CameoServiceImpl extends RemoteServiceServlet implements CameoService {

	static ArrayList<CameoEntry> cameoList = new ArrayList<>();

	static {
		ObjectifyService.register(CameoEntry.class);
        ObjectifyService.register(CameoRule.class);
        ObjectifyService.register(CameoTranslatedRule.class);
        ObjectifyService.register(Word.class);
        ObjectifyService.register(VerdictOnRule.class);
        ObjectifyService.register(Submission.class);
	}
	
	
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		if (ofy().load().type(CameoEntry.class).count() == 0) {
			Gson gson = new Gson();
            log("LOading Data");
			try {
				Type type = new TypeToken<ArrayList<CameoEntry>>() {
				}.getType();
				ArrayList<CameoEntry> cameoList = gson.fromJson(new FileReader("cameo.json"), type);
				ofy().save().entities(cameoList).now();
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (ofy().load().type(CameoRule.class).count() == 0) {
			Gson gson = new Gson();
            log("LOading Data");
			try {
				Type type = new TypeToken<ArrayList<CameoRule>>() {
				}.getType();
				ArrayList<CameoRule> cameoRules = gson.fromJson(new FileReader("OutputRule.json"), type);
				BufferedReader br = new BufferedReader(new FileReader("translated_rules.txt"));
				for (CameoRule rule: cameoRules){
					ofy().save().entity(rule).now();
					br.readLine();
					CameoTranslatedRule transRule = new CameoTranslatedRule("es", br.readLine().trim(), "google", false);
					transRule.setRuleID(rule.getId());
					ofy().save().entity(transRule).now();
				}
				
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (ofy().load().type(CameoTranslatedRule.class).count() == 0){
			List<CameoRule> rules = ofy().load().type(CameoRule.class).list();
			ArrayList<CameoTranslatedRule> tRules = new ArrayList<>();
			for (CameoRule rule: rules){
				CameoTranslatedRule tRule = new CameoTranslatedRule("es", "Dummy "+rule.getId(), "Dummy", false);
				tRule.setRuleID(rule.getId());
				tRules.add(tRule);
			}
			
			ofy().save().entities(tRules).now();
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
					                     .filter("word", word).list();
			
			return new ArrayList<>(rules);
		}
		
		return null;
	}

	@Override
	public ArrayList<CameoTranslatedRule> getCameoTranslation(CameoRule rule, String langCode) {
		// TODO Auto-generated method stub
		
		List<CameoTranslatedRule> list = ofy().load().type(CameoTranslatedRule.class).filter("ruleID", rule.getId()).filter("languageCode", langCode).list();
		
		log("Translated rules sent; "+list.size());
		return new ArrayList<>(list);
	}

	@Override
	public List<String> getCameoSummery() {
		// TODO Auto-generated method stub
		List<CameoEntry> list = ofy().load().type(CameoEntry.class).list();
		
		List<String> summeries = new ArrayList<>();
		for (CameoEntry e: list) {
			summeries.add(e.summery());
			log(e.summery());
		}
		
		return summeries;
	}

	@Override
	public ArrayList<String> getRelatedWords(String cameoCode) {
		// TODO Auto-generated method stub
		List<CameoRule> rules = ofy().load().type(CameoRule.class).filter("cameoCode",cameoCode).project("word").distinct(true).list();
		ArrayList<String> words = new ArrayList<>();
		for (CameoRule rule: rules){
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
	
	

}
