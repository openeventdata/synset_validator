package edu.utd.cs.bdma.synset.validator.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.googlecode.objectify.ObjectifyService;

import static com.googlecode.objectify.ObjectifyService.ofy;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetExample;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetSelection;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetVerdict;

public class DatabaseUpdater extends HttpServlet{
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		ObjectifyService.register(CameoRule.class);
		ObjectifyService.register(SynsetEntry.class);
		ObjectifyService.register(SynsetExample.class);
		ObjectifyService.register(Submission.class);
		ObjectifyService.register(Submission.class);
		ObjectifyService.register(SynsetEntry.class);
		ObjectifyService.register(SynsetVerdict.class);
		ObjectifyService.register(CameoSelectedSynset.class);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		
	    //resp.getOutputStream().print(addNewCameoRules());
		//resp.getOutputStream().print(addExamples());
		
		//resp.getOutputStream().print(updateSynsetSelections());
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	private String addNewCameoRules() throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		HashSet<String> ruleSet = new HashSet<String>();
		List<CameoRule> rulesFromDB = ofy().load().type(CameoRule.class).list();
		
		for (CameoRule rule: rulesFromDB){
			ruleSet.add(rule.toString());
		}
		
		CameoRule[] rulesExtended = new Gson().fromJson(new FileReader("CameoRulesExt.json"), CameoRule[].class);
		
		ArrayList<CameoRule>  newRules = new ArrayList<>();
		
		for (CameoRule rule: rulesExtended){
			if (ruleSet.contains(rule.toString())){
				//do nothing
			} else {
				newRules.add(rule);
			}
		}
		
		ofy().save().entities(newRules).now();
		
		
		
		return "Update Completed. Number of entries entered into the database is " + newRules.size();
	}
	
	private String addExamples(){
		
		if (ofy().load().type(SynsetExample.class).count() == 0){
			List<SynsetEntry> synsetList = ofy().load().type(SynsetEntry.class).list();
			ArrayList<SynsetExample> examplesList = new ArrayList<>();
			
			for (SynsetEntry entry: synsetList){
				ArrayList<String> examples = getExamples(entry.getGloss());
				for (String s: examples){
					SynsetExample synsetExample = new SynsetExample();
					synsetExample.setExample(s);
					synsetExample.setSynsetId(entry.getId());
					examplesList.add(synsetExample);
				}
				
			}
			
			ofy().save().entities(examplesList).now();
			
			return "New Examples "+examplesList.size();
			
		} else {
			return "Process already executed";
		}
		
	}
	
	private ArrayList<String> getExamples(String data){
		String[] words = data.split("\"");
		ArrayList<String> examples = new ArrayList<>();
		StringBuilder sb = new StringBuilder(words[0]);
		for (int i = 1; i < words.length; i+= 2){
			examples.add(words[i].trim());		       
		}
		return examples;
	}
	
	private String updateSynsetSelections(){
	   List<Submission> submissions = ofy().load().type(Submission.class).list();
	   ArrayList<SynsetVerdict> synsetVerdicts = new ArrayList<>();
	   
	   for (Submission submission: submissions){
		   List<SynsetEntry> entries = ofy().load().type(SynsetEntry.class).filter("idWord", submission.getWordId()).list();
		   
		   List<CameoSelectedSynset> selectedSynsets = ofy().load().type(CameoSelectedSynset.class).filter("submissionId", submission.getId()).list();
		   
		   HashSet<Long> idsSS = new HashSet<>();
		   for (CameoSelectedSynset selsyn: selectedSynsets) idsSS.add(selsyn.getIdSynsetEntry());
		   
		   for (SynsetEntry entry: entries){
			   SynsetVerdict sv = new SynsetVerdict();
			   sv.setSubmissionId(submission.getId());
			   sv.setSynsetId(entry.getId());
			   
			   if (idsSS.contains(entry.getId())){
				   sv.setVerdict(SynsetVerdict.CORRECT);
			   } else {
				  sv.setVerdict(SynsetVerdict.INCORRECT); 
			   }
			   synsetVerdicts.add(sv);
		   }
	   }
	   
	   ofy().save().entities(synsetVerdicts).now();
	   return "Number of verdicts saved is "+synsetVerdicts.size();
	}

}
