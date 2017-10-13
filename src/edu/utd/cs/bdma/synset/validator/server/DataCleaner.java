package edu.utd.cs.bdma.synset.validator.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DataCleaner extends HttpServlet{
	
	static{
		ObjectifyService.register(SynsetWord.class);
		ObjectifyService.register(CameoRule.class);
		
		
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
	    //cleanDictionary(resp);
		
		resp.getOutputStream().println("Operation Completed");
	}
	
	private void cleanDictionary(HttpServletResponse resp) throws JsonSyntaxException, IOException{
		Gson gson1 = new Gson();
		CameoRule[] lists = gson1.fromJson(DataLoader.getContentAsString("OutputRule.json"), CameoRule[].class);
		
		HashSet<String> rulesSet = new HashSet<>();
		for (CameoRule rule: lists){
			rulesSet.add(rule.toString());
		}
		resp.getOutputStream().println("Number of rules: "+ rulesSet.size());
		List<CameoRule> existingRules = ofy().load().type(CameoRule.class).list();
	    ArrayList<CameoRule> deletedRules = new ArrayList<>();	
		for (CameoRule r: existingRules){
			if (!rulesSet.contains(r.toString())){
				deletedRules.add(r);
			}
		}
		
		ofy().delete().entities(deletedRules).now();
		
		int numRules = ofy().load().type(CameoRule.class).count();
		resp.getOutputStream().println("Number of rules (after Deletion): "+ numRules);
		
		
	}
	
	private void cleanWords(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        List<SynsetWord> words = ofy().load().type(SynsetWord.class).list();
		
		HashSet<String> filteredWords = new HashSet<>();
		
		for (SynsetWord w: words){
			String ws  = w.getWord()+"_"+w.getIdSynsetEntry();
			if (filteredWords.contains(ws)){
				ofy().delete().entity(w).now();
				resp.getOutputStream().println(ws+"<br/>");
				System.out.println("Deleted "+ ws);
			} else
			{
				filteredWords.add(ws);
			}
		}
		
	}

}
