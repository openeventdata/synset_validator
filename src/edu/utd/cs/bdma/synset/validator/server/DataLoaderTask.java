package edu.utd.cs.bdma.synset.validator.server;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class DataLoaderTask implements DeferredTask{

	static {
		ObjectifyService.begin();
		ObjectifyService.register(CameoEntry.class);
        ObjectifyService.register(CameoRule.class);
        ObjectifyService.register(CameoTranslatedRule.class);
        ObjectifyService.register(Word.class);
		ObjectifyService.register(SynsetEntry.class);
		ObjectifyService.register(SynsetWord.class);
	
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("CALLED DEFFERED TASK");
		try {
			loadData();
			loadCameoData();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadCameoData(){
		if (ofy().load().type(CameoEntry.class).count() == 0) {
			Gson gson = new Gson();
            System.out.println("Loading Data");
			try {
				Type type = new TypeToken<ArrayList<CameoEntry>>() {
				}.getType();
				ArrayList<CameoEntry> cameoList = gson.fromJson(new InputStreamReader(new FileInputStream("cameo.json"), "UTF-8"), type);
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
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (ofy().load().type(CameoRule.class).count() == 0) {
			Gson gson = new Gson();
            System.out.println("LOading Data");
			try {
				Type type = new TypeToken<ArrayList<CameoRule>>() {
				}.getType();
				ArrayList<CameoRule> cameoRules = gson.fromJson(new InputStreamReader(new FileInputStream("OutputRule.json"), "UTF-8"), type);
				System.out.println("NO. of CameoRules: "+cameoRules.size());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("translated_rules.txt"), "UTF-8"));
				ArrayList<CameoTranslatedRule> tRules = new ArrayList<>();
				for (CameoRule rule: cameoRules){
					ofy().save().entity(rule).now();
					br.readLine();
					CameoTranslatedRule transRule = new CameoTranslatedRule("es", br.readLine().trim(), "google", false);
					transRule.setRuleID(rule.getId());
					tRules.add(transRule);
				}
				System.out.println("No of Translated Rules: "+tRules.size());
				ofy().save().entities(tRules).now();
				
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
		System.out.println("Number of CameoEntry: "+ ofy().load().type(CameoEntry.class).count());
		System.out.println("Number of CameoRules "+ ofy().load().type(CameoRule.class).count());
		System.out.println("Number of Translated Rules: "+ ofy().load().type(CameoTranslatedRule.class).count());
		

	}
	
	private void loadData() throws JsonSyntaxException, IOException {
		HashMap<String, ArrayList<SynsetEntry2>> mapSynsets = new HashMap<>();
		System.out.println("Loading Data=====================");
		Gson gson1 = new Gson();
		SynsetEntry2[] lists = gson1.fromJson(getContentAsString("Output.json"), SynsetEntry2[].class);
		for (SynsetEntry2 s : lists) {
			System.out.println(s.getSets()[0].getWords().get(0));
			ArrayList<SynsetEntry2> list = mapSynsets.get(s.getConcept());
			if (list == null)
				list = new ArrayList<>();
			list.add(s);
			mapSynsets.put(s.getConcept(), list);
		}

		if ((ofy().load().type(Word.class).count() != 0)) {

		} else {
			for (String wordText : mapSynsets.keySet()) {

				Word word = new Word(wordText);

				ofy().save().entity(word).now();

				ArrayList<SynsetEntry2> list = mapSynsets.get(wordText);
                ArrayList<SynsetWord> synsetWords = new ArrayList<>();
				for (SynsetEntry2 s : list) {
					SynsetEntry s2 = new SynsetEntry(s.getGloss(), "wordnet");
					s2.setIdWord(word.getId());
					ofy().save().entity(s2).now();

					for (Synset syns : s.getSets()) {
						for (String w : syns.getWords()) {
							SynsetWord sw = new SynsetWord(w, syns.getLang());
							sw.setIdSynsetEntry(s2.getId());
							synsetWords.add(sw);
						}
					}
					
				}
				ofy().save().entities(synsetWords).now();
				System.out.println(synsetWords.size()+ " synset words inserted.");

			}
		}
		
		System.out.println("Number of Words: "+ ofy().load().type(Word.class).count());
		System.out.println("Number of Synset entries "+ ofy().load().type(SynsetEntry.class).count());
		System.out.println("Number of Synset Words: "+ ofy().load().type(SynsetWord.class).count());
		//System.out.println("Number of Cameo Entries: "+ ofy().load().type(CameoEntry.class).count());
	}

	private static String getContentAsString(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		while (br.ready()) {
			sb.append(br.readLine());
		}
		br.close();

		return sb.toString();
	}

}
