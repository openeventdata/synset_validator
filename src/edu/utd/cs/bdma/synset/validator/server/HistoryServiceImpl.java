package edu.utd.cs.bdma.synset.validator.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.client.HistoryService;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.SubmissionDetails;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.VerdictOnRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class HistoryServiceImpl extends RemoteServiceServlet implements HistoryService{

	static {
		ObjectifyService.register(CameoEntry.class);
        ObjectifyService.register(CameoRule.class);
        ObjectifyService.register(CameoTranslatedRule.class);
        ObjectifyService.register(Word.class);
        ObjectifyService.register(VerdictOnRule.class);
        ObjectifyService.register(Submission.class);
	}
	
	@Override
	public ArrayList<SubmissionDetails> getSubmissions() {
		// TODO Auto-generated method stub
		UserInfo user = (UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user");
		ArrayList<SubmissionDetails> submissions = new ArrayList<SubmissionDetails>();
		if (user != null){
			List<Submission> submitList = ofy().load().type(Submission.class).filter("userId", user.getId()).list();
			
			for (Submission submission: submitList){
				if (submission.getWordId() == null) continue;
				CameoEntry entry = ofy().load().type(CameoEntry.class).filterKey(KeyFactory.createKey("CameoEntry", submission.getCameoId())).first().now();
				Word word = ofy().load().type(Word.class).filterKey(KeyFactory.createKey("Word", submission.getWordId())).first().now();
				SubmissionDetails det = new SubmissionDetails();
				det.setCameoCode(entry.getCode());
				det.setCameoDef(entry.getConcept());
				det.setCameoDescription(entry.getDescription());
				det.setWord(word.getText());
				det.setSubmissionId(submission.getId());
				
				submissions.add(det);
				
			}
			HashSet<SubmissionDetails> hset = new HashSet<>(submissions);
			submissions = new ArrayList<>(hset);
			log("Number of submisssions: "+ submissions.size());
			return submissions;
		}
		return null;
	}

}
