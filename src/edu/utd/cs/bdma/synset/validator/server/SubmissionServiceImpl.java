package edu.utd.cs.bdma.synset.validator.server;

import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.client.SubmissionService;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.FeedbackOnSynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWordWithFeedback;
import edu.utd.cs.bdma.synset.validator.shared.entity.UpdatedInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.VerdictOnRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService {

	static {
		ObjectifyService.register(Word.class);
		ObjectifyService.register(SynsetEntry.class);
		ObjectifyService.register(SynsetWord.class);
		ObjectifyService.register(CameoSelectedSynset.class);
		ObjectifyService.register(CameoEntry.class);
		ObjectifyService.register(FeedbackOnSynsetWord.class);
		ObjectifyService.register(VerdictOnRule.class);
		ObjectifyService.register(Submission.class);
		ObjectifyService.register(CameoTranslatedRule.class);
	}

	@Override
	public void submit(UpdatedInfo info, String cameoCode, String word) {
		// TODO Auto-generated method stub
		//log("Working without inserting");
		// TODO Auto-generated method stub
		Submission submission = create(cameoCode, word);
		Long submissionId = submission.getId();
		for (VerdictOnRule v : info.getVerdictsOnRules()) {
			// v.setIdUser(userID);
			v.setSubmissionId(submissionId);
		}
		ofy().save().entities(info.getVerdictsOnRules()).now();
		
		
		for (CameoSelectedSynset selection : info.getCameoSelectedSynsets()) {
			selection.setSubmissionId(submissionId);
		}
		ofy().save().entities(info.getCameoSelectedSynsets()).now();
		
		for (FeedbackOnSynsetWord feedback : info.getFeedbackOnSynsetWords()) {
			feedback.setSubmissionId(submissionId);
		}
        ofy().save().entities(info.getFeedbackOnSynsetWords()).now();
        
        
        for (SynsetEntryWithWords newEntry : info.getSynsetEntryWithWords()) {
			SynsetEntry entry = newEntry.getEntry();
			entry.addInfo(submission);
			ofy().save().entity(entry).now();

			CameoSelectedSynset selection = new CameoSelectedSynset();
			selection.setSubmissionId(submissionId);
			selection.setIdSynsetEntry(entry.getId());
			ofy().save().entity(selection).now();

			for (SynsetWord w : newEntry.getWords()) {
				w.setIdSynsetEntry(entry.getId());
				w.setSubmissionId(submissionId);
				ofy().save().entity(w).now();
			}

			for (SynsetWord w : newEntry.getWords()) {
				FeedbackOnSynsetWord feedback = new FeedbackOnSynsetWord();
				feedback.setIdWord(w.getId());
				feedback.setSubmissionId(submissionId);
				feedback.setVerdict(FeedbackOnSynsetWord.CORRECT);
				ofy().save().entity(feedback).now();
			}

		}
        log("Number of new words added: "+ info.getNewWords().size());
        for (SynsetWordWithFeedback w : info.getNewWords()){
        	w.getSynsetWord().setSubmissionId(submissionId);
        	
        	ofy().save().entity(w.getSynsetWord()).now();
        	log(w.getSynsetWord().getWord()+w.getSynsetWord().getIdSynsetEntry());
        	w.getFeedback().setSubmissionId(submissionId);
        	w.getFeedback().setIdWord(w.getSynsetWord().getId());
        	ofy().save().entity(w.getFeedback()).now();
        	
        }
			
		
		
		
		log("All data inserted successfully");

	}
	
	
	

    private Submission create(String cameoCode, String word){
    	CameoEntry entry = ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
    	Word wordEntry = ofy().load().type(Word.class).filter("text", word).first().now();
    	UserInfo user = (UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user");
    	
//    	Submission submission = ofy().load().type(Submission.class)
//    			.filter("cameoId", entry.getId())
//    			.filter("userId", user.getId())
//    			.filter("wordId", wordEntry.getId()).first().now();
//    	
//    	if (submission == null){
	    	Submission submission = new Submission();
	    	submission.setCameoId(entry.getId());
	    	submission.setUserId(user.getId());
	    	submission.setWordId(wordEntry.getId());
	    	submission.setTime(new Date(System.currentTimeMillis()));
	    	
	    	ofy().save().entity(submission).now();
//    	} else {
//    		log("Submission Located "+submission.getId());
//    		List<VerdictOnRule> verdicts = ofy().load().type(VerdictOnRule.class).filter("submissionId", submission.getId()).list();
//    		ofy().delete().entities(verdicts).now();
//    		
//    		List<CameoSelectedSynset> selectedSynsets = ofy().load()
//    				                                   .type(CameoSelectedSynset.class)
//    				                                   .filter("submissionId", submission.getId()).list();
//    		
//    		ofy().delete().entities(selectedSynsets).now();
//    		
//    		List<SynsetWord> newWords = ofy().load().type(SynsetWord.class).filter("submissionId", submission.getId()).list();
//    		
//    		ofy().delete().entities(newWords).now();
//    		
//    		List<FeedbackOnSynsetWord> feedbacks = ofy().load().type(FeedbackOnSynsetWord.class).filter("submissionId", submission.getId()).list();
//
//    		log("Size of Feedbacks; "+feedbacks.size());
//    		
//    		ofy().delete().entities(feedbacks).now();
//    		
//    		List<SynsetEntry> newSynEntries = ofy().load().type(SynsetEntry.class).filter("submissionId", submission.getId()).list();
//            ofy().delete().entities(newSynEntries).now();
//        }
    	
    	return submission;
    	
    }
	

	@Override
	public UpdatedInfo retrieve(Long submissionId) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public UpdatedInfo retrieve(String cameoCode, String word) {
		// TODO Auto-generated method stub
		CameoEntry entry = ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
    	Word wordEntry = ofy().load().type(Word.class).filter("text", word).first().now();
    	UserInfo user = (UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user");
    	Submission submission = null;
    	try{
    		List<Submission> submissions = 
    	               ofy().load().type(Submission.class)
    			.filter("cameoId", entry.getId())
    			.filter("userId", user.getId())
    			.filter("wordId", wordEntry.getId()).list();
    		
    		Collections.sort(submissions, new Comparator<Submission>() {

				@Override
				public int compare(Submission o1, Submission o2) {
					// TODO Auto-generated method stub
					return o2.getTime().compareTo(o1.getTime());
				}
			});
    		log("SIZE OF SUBMISSIONS: "+submissions.size());
    		if (submissions.size() != 0){
    			submission = submissions.get(0);
    			log(""+submission.getTime().toString());
    		}
    		
    	}catch(NullPointerException ex){
    		return new UpdatedInfo();
    	}
    	if (submission != null){
    		log("Submission Located "+submission.getId());
    		UpdatedInfo info = new UpdatedInfo();
    		List<VerdictOnRule> verdicts = ofy().load().type(VerdictOnRule.class).filter("submissionId", submission.getId()).list();
    		if (verdicts != null) info.setVerdictsOnRules(new ArrayList<>(verdicts));
    		
    		List<CameoSelectedSynset> selectedSynsets = ofy().load()
    				                                   .type(CameoSelectedSynset.class)
    				                                   .filter("submissionId", submission.getId()).list();
    		
    		info.setCameoSelectedSynsets(new ArrayList<>(selectedSynsets));
    		
    		//List<SynsetWord> newWords = ofy().load().type(SynsetWord.class).filter("submissionId", submission.getId()).list();
    		
    		//info.setNewWords(new ArrayList<>(newWords));
    		
    		List<FeedbackOnSynsetWord> feedbacks = ofy().load().type(FeedbackOnSynsetWord.class).filter("submissionId", submission.getId()).list();

    		log("Size of Feedbacks; "+feedbacks.size());
    		
    		info.setFeedbackOnSynsetWords(new ArrayList<>(feedbacks));
    		
    		//List<SynsetEntry> newSynEntries = ofy().load().type(SynsetEntry.class).filter("submissionId", submission.getId()).list();

    	    
    		
    		ArrayList<SynsetEntryWithWords> entries = new ArrayList<>();
    		
//    		for (SynsetEntry e: newSynEntries){
//    			
//    			entries.add(new SynsetEntryWithWords(e));
//    		}
    		
//    		log("Number of Words: "+entries.get(0).getWords().size());
//    		
//    		for (SynsetWord w: newWords){
//    			boolean added = false;
//                for (SynsetEntryWithWords sw: entries){
//                	if (w.getIdSynsetEntry() == sw.getEntry().getId()){
//                		sw.addWord(w);
//                		added = true;
//                		break;
//                	}
//                }
//                if (!added){
//                	newWordsExistingSynset.add(w);
//                }
//                
//    		}
//    		
//    		info.setSynsetEntryWithWords(entries);
    		//info.setNewWords(newWordsExistingSynset);
    		
    		return info;
    	}
    	return new UpdatedInfo();
	}

}
