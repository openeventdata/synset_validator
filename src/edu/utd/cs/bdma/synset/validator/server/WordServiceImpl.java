package edu.utd.cs.bdma.synset.validator.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.FeedbackOnSynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;

public class WordServiceImpl extends RemoteServiceServlet implements WordService {

    static ArrayList<String> countries = new ArrayList<>();
	
	static {
		try {
			BufferedReader br = new BufferedReader(new FileReader("countries"+"_es"+".txt"));
			while (br.ready()){
				countries.add(br.readLine().split("\t")[0].trim());
			}
			Collections.sort(countries);
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
		try {
			loadData();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		List<SynsetWord> words = ofy().load().type(SynsetWord.class).filter("idSynsetEntry", entry.getId())
				.filter("languageCode", langCode).list();
		if (words != null) {
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
		BufferedReader br = new BufferedReader(new FileReader(filename));
		StringBuilder sb = new StringBuilder();
		while (br.ready()) {
			sb.append(br.readLine());
		}
		br.close();

		return sb.toString();
	}

	@Override
	public ArrayList<SynsetEntryWithWords> getAll(String word, String langCode) {
		// TODO Auto-generated method stub
		ArrayList<SynsetEntry> entries = getSynsets(word);
		if (entries != null)
			log("Synset Entry length " + entries.size());
		ArrayList<SynsetEntryWithWords> entriesWithWords = new ArrayList<>();
		if (entries != null) {
			for (SynsetEntry entry : entries) {
				SynsetEntryWithWords entryWithWord = new SynsetEntryWithWords(entry);
				entryWithWord.addAllWords(getWords(entry, langCode));
				entriesWithWords.add(entryWithWord);
			}
			log("Synset Entry with Word length " + entriesWithWords.size());
			log("First words: "+entriesWithWords.get(0).getWords().size());
			log("First ID: "+ entriesWithWords.get(0).getEntry().getId());

		}
		return entriesWithWords;
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
	public int addSynset(ArrayList<SynsetEntryWithWords> newEntries, String cameoCode, String wordText) {
		// TODO Auto-generated method stub
		Submission submission = getSubmission();
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
				ofy().save().entity(word).now();
			}

			for (SynsetWord word : newEntry.getWords()) {
				FeedbackOnSynsetWord feedback = new FeedbackOnSynsetWord();
				feedback.setIdWord(word.getId());
				feedback.setSubmissionId(submission.getId());
				feedback.setVerdict(FeedbackOnSynsetWord.CORRECT);
				ofy().save().entity(feedback).now();
			}

		}

		return 1;
	}

	@Override
	public int addSynsetWord(ArrayList<SynsetWord> words) {
		// TODO Auto-generated method stub
		log("Got Newwords");
		for (SynsetWord w : words)
			w.setSubmissionId(getSubmission().getId());
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
		// TODO Auto-generated method stub
		return countries;
	}

}
