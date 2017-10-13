package edu.utd.cs.bdma.synset.validator.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.client.WordService;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.FeedbackOnSynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetExample;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetStatistics;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;

public class WordServiceImpl extends RemoteServiceServlet implements WordService {

    static HashMap<String, ArrayList<String>> countriesMap = new HashMap<>();
    
    static HashMap<Long, HashMap<Long, SynsetStatistics>> cameoToSynsetStat = new HashMap<>();
	
	static {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("countries"+"_es"+".txt"), "UTF-8"));
			while (br.ready()){
				String line = br.readLine().trim();
				String[] words = line.split(","); 
				try{
					countriesMap.get(words[0]).add(words[1]);
				}catch(NullPointerException ex){
					ArrayList<String> temp = new ArrayList<>();
					temp.add(words[1]);
					countriesMap.put(words[0], temp);
							
				}
			}
			br.close();
			
			SynsetStatistics[] stats = new Gson().fromJson(new FileReader("SynsetStatistics.json"), SynsetStatistics[].class);
			
			for (SynsetStatistics s: stats){
				if (!cameoToSynsetStat.containsKey(s.getCameoId())){
					cameoToSynsetStat.put(s.getCameoId(), new HashMap<Long, SynsetStatistics>());
				} 
				
				HashMap<Long, SynsetStatistics> synsetToStat = cameoToSynsetStat.get(s.getCameoId());
				
				synsetToStat.put(s.getSynsetId(), s);
			}
			
			System.out.println("Loaded the synset validation information.");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		ObjectifyService.register(Word.class);
		ObjectifyService.register(SynsetEntry.class);
		ObjectifyService.register(SynsetWord.class);
		ObjectifyService.register(CameoSelectedSynset.class);
		ObjectifyService.register(FeedbackOnSynsetWord.class);
		ObjectifyService.register(CameoRule.class);
		ObjectifyService.register(CameoEntry.class);
		ObjectifyService.register(SynsetExample.class);
//		try {
//			loadData();
//		} catch (JsonSyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	@Override
	public ArrayList<SynsetEntry> getSynsets(String word) {
		// TODO Auto-generated method stub
		Word wordEntry = ofy().load().type(Word.class).filter("text", word).first().now();
		
		if (wordEntry != null) {
			clearSubmissionID();
			addWordToSession(wordEntry);
			List<SynsetEntry> list = ofy().load().type(SynsetEntry.class).filter("idWord", wordEntry.getId()).list();
			return new ArrayList<>(list);
		} else
			return null;
	}

	@Override
	public ArrayList<SynsetWord> getWords(SynsetEntry entry, String langCode) {
		// TODO Auto-generated method stub
		String lCode = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getLanguage();
		
		log("Loading entries for Language: "+ lCode);
		List<SynsetWord> words = ofy().load().type(SynsetWord.class).filter("idSynsetEntry", entry.getId())
				.filter("languageCode", lCode).list();
		if (words != null) {
			System.out.println(words);
			return new ArrayList<>(words);
		} else {
			return null;
		}
	}

	private void loadData() throws JsonSyntaxException, IOException {
		HashMap<String, ArrayList<SynsetEntry2>> mapSynsets = new HashMap<>();
		log("Loading Data=====================");
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

				for (SynsetEntry2 s : list) {
					SynsetEntry s2 = new SynsetEntry(s.getGloss(), "wordnet");
					s2.setIdWord(word.getId());
					ofy().save().entity(s2).now();

					for (Synset syns : s.getSets()) {
						for (String w : syns.getWords()) {
							SynsetWord sw = new SynsetWord(w, syns.getLang());
							sw.setIdSynsetEntry(s2.getId());
							ofy().save().entity(sw).now();
						}
					}
				}

			}
		}
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
	
	private ArrayList<SynsetExample> getExamples(SynsetEntry entry)
	{
		List<SynsetExample> examples = ofy().load().type(SynsetExample.class).filter("synsetId", entry.getId()).list();
		log("Examples Size: " + examples.size());
		return new ArrayList<>(examples);
	}
	
	@Override
	public ArrayList<SynsetEntryWithWords> getAll(String word, String cameoCode, String langCode) {
		// TODO Auto-generated method stub
		Long userID = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getId();
		ArrayList<SynsetEntry> entries = getSynsets(word);
		String userLang = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getLanguage();
		
		if (entries != null)
			log("Synset Entry length " + entries.size());
		
		
		
		ArrayList<SynsetEntryWithWords> entriesWithWords = new ArrayList<>();
		if (entries != null) {
			if (userLang != "es") entries = filter(entries, cameoCode);
			for (SynsetEntry entry : entries) {
				SynsetEntryWithWords entryWithWord = new SynsetEntryWithWords(entry);
				entryWithWord.addAllWords(getWords(entry, langCode));
				entryWithWord.setExamples(getExamples(entry));
				//entryWithWord.setReadOnly((""+userID).equals(entry.getSource()));
				entriesWithWords.add(entryWithWord);
			}
			if (entriesWithWords.size()>0){
				log("Synset Entry with Word length " + entriesWithWords.size());
				log("First words: " + entriesWithWords.get(0).getWords().size());
				log("First ID: " + entriesWithWords.get(0).getEntry().getId());
			}
		}
		return entriesWithWords;
	}

	private ArrayList<SynsetEntry> filter(ArrayList<SynsetEntry> entries, String cameoCode) {
		// TODO Auto-generated method stub
		//return entries;
		CameoEntry cameoEntry = ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
		ArrayList<SynsetEntry> filteredList = new ArrayList<>();
		if (cameoEntry != null){
			HashMap<Long, SynsetStatistics> synsetToStat = cameoToSynsetStat.get(cameoEntry.getId());
			if (synsetToStat != null){
			for (SynsetEntry se: entries){
				if (!synsetToStat.containsKey(se.getId())){
					SynsetStatistics st = synsetToStat.get(se.getId());
					if (st.getCountCorrect() >= st.getCountAmbiguous()+st.getCountInCorrect()){
						filteredList.add(se);
					}
				} else {
					filteredList.add(se);
				}
			}
			}else{
				filteredList.addAll(entries);
			}
		}
		
		return filteredList;
	}

	@Override
	public int addSynsetSelections(ArrayList<CameoSelectedSynset> selections, String cameoCode) {
		// TODO Auto-generated method stub
        
		Submission submission = getSubmission();

			for (CameoSelectedSynset selection : selections) {
				selection.setSubmissionId(submission.getId());
			}
			Map<Key<CameoSelectedSynset>, CameoSelectedSynset> map = ofy().save().entities(selections).now();
			return map.keySet().size();
		

	}

	@Override
	public int addFeedbackOnWords(ArrayList<FeedbackOnSynsetWord> feedbacks, String cameoCode) {
		// TODO Auto-generated method stub
		Submission submission = getSubmission();
		
			for (FeedbackOnSynsetWord feedback : feedbacks) {
				feedback.setSubmissionId(submission.getId());
			}
			Map<Key<FeedbackOnSynsetWord>, FeedbackOnSynsetWord> map = ofy().save().entities(feedbacks).now();
			return map.keySet().size();
		

	}

	@Override
	public ArrayList<SynsetEntryWithWords> addSynset(ArrayList<SynsetEntryWithWords> newEntries, String cameoCode, String wordText) {
		// TODO Auto-generated method stub
		Submission submission = getSubmission();
		UserInfo user = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user"));

		for (SynsetEntryWithWords newEntry : newEntries) {
			SynsetEntry entry = newEntry.getEntry();
			entry.addInfo(submission);
			
			ofy().save().entity(entry).now();

			CameoSelectedSynset selection = new CameoSelectedSynset();
			selection.setSubmissionId(submission.getId());
			selection.setIdSynsetEntry(entry.getId());
			ofy().save().entity(selection).now();

			for (SynsetWord word : newEntry.getWords()) {
				word.setIdSynsetEntry(entry.getId());
				word.setSubmissionId(submission.getId());
				word.setLanguageCode(user.getLanguage());
				ofy().save().entity(word).now();
			}

			for (SynsetWord word : newEntry.getWords()) {
				FeedbackOnSynsetWord feedback = new FeedbackOnSynsetWord();
				feedback.setIdWord(word.getId());
				feedback.setSubmissionId(submission.getId());
				feedback.setVerdict(FeedbackOnSynsetWord.CORRECT);
				ofy().save().entity(feedback).now();
			}
			
			for (SynsetExample ex: newEntry.getExamples()){
				ex.setSynsetId(entry.getId());
				ex.setSource(""+submission.getUserId());
				ofy().save().entity(ex).now();
			}

		}

		return newEntries;
	}

	@Override
	public int addSynsetWord(ArrayList<SynsetWord> words) {
		// TODO Auto-generated method stub
		log("Got Newwords");
		UserInfo user = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user"));
		for (SynsetWord w : words){
			w.setSubmissionId(getSubmission().getId());
			w.setLanguageCode(user.getLanguage());
		}
			
		return ofy().save().entities(words).now().size();
	}

	private void clearSubmissionID() {
		Submission submission = (Submission) this.getThreadLocalRequest().getSession().getAttribute("submission");

		if (submission != null) {
			this.getThreadLocalRequest().getSession().setAttribute("submission", null);
		}
	}

	private void addWordToSession(Word word) {
		this.getThreadLocalRequest().getSession().setAttribute("word", word);
	}

	private synchronized Submission getSubmission() {
		Submission submission = (Submission) this.getThreadLocalRequest().getSession().getAttribute("submission");
		Long userID = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getId();
		Long cameoId = ((CameoEntry) this.getThreadLocalRequest().getSession().getAttribute("cameo")).getId();
		Long wordId = ((Word) this.getThreadLocalRequest().getSession().getAttribute("word")).getId();
		if (submission == null) {
			log("No submission is found in the session");
			submission = new Submission();
			submission.setUserId(userID);
			submission.setCameoId(cameoId);
			submission.setWordId(wordId);
			ofy().save().entity(submission).now();
			this.getThreadLocalRequest().getSession().setAttribute("submission", submission);
		}

		return submission;

	}

	@Override
	public ArrayList<String> getCountries(String langCode) {
		// TODO Auto-generated method stub\
		String lCode = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getLanguage();
		return countriesMap.get(lCode);
	}

	@Override
	public ArrayList<SynsetExample> addExamples(SynsetEntry entry, String[] examples) {
		// TODO Auto-generated method stub
		Long userID = ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getId();
		ArrayList<SynsetExample> newExamples = new ArrayList<>();
		
		for (String example: examples){
			SynsetExample ex = new SynsetExample();
			ex.setExample(example);
			ex.setSource(""+userID);
			ex.setSynsetId(entry.getId());
			newExamples.add(ex);
		}
		
		ofy().save().entities(newExamples).now();
		
		return newExamples;
	}

	@Override
	public String getCodingLanguage() {
		// TODO Auto-generated method stub
		return ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user")).getLanguage();
	}

	
}
