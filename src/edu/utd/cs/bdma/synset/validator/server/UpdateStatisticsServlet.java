package edu.utd.cs.bdma.synset.validator.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.Submission;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetStatistics;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetVerdict;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class UpdateStatisticsServlet extends HttpServlet{

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		ObjectifyService.register(SynsetStatistics.class);
		ObjectifyService.register(CameoEntry.class);
		ObjectifyService.register(Word.class);
		ObjectifyService.register(SynsetVerdict.class);
		ObjectifyService.register(SynsetEntry.class);
		ObjectifyService.register(Submission.class);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		updateSynsetStatistics();
		resp.getOutputStream().write("Update Completed".getBytes());
			
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	private void updateSynsetStatistics(){
        List<SynsetVerdict> sverdicts = ofy().load().type(SynsetVerdict.class).list();
		
		
		
		List<Submission> submissions = ofy().load().type(Submission.class).list();
		
		HashMap<Long, Submission> idToSubmissions = new HashMap<>();
		
		for (Submission s: submissions){
			idToSubmissions.put(s.getId(), s);
		}
		
		HashMap<Long,HashMap<Long, SynsetStatistics>> cameoToSynsetStat = new HashMap<>();
		
		for (SynsetVerdict sv: sverdicts){
			Submission submission = idToSubmissions.get(sv.getSubmissionId());
			if (submission == null) continue;
			
			Long cameoId = submission.getCameoId();
		
			if (!cameoToSynsetStat.containsKey(cameoId)) 
				cameoToSynsetStat.put(cameoId, new HashMap<Long, SynsetStatistics>());
			
			HashMap<Long, SynsetStatistics> synsetToStatistics = cameoToSynsetStat.get(cameoId);
			
			if (!synsetToStatistics.containsKey(sv.getId())){
				SynsetStatistics st = new SynsetStatistics();
			    st.setCameoId(cameoId);
			    st.setSynsetId(sv.getId());
			    
				synsetToStatistics.put(sv.getId(), st);
			}
			
			SynsetStatistics st = synsetToStatistics.get(sv.getId());
			
			switch (sv.getVerdict()){
			case SynsetVerdict.CORRECT: st.setCountCorrect(st.getCountCorrect()+1); break;
			case SynsetVerdict.INCORRECT: st.setCountInCorrect(st.getCountInCorrect()+1); break;
			case SynsetVerdict.AMBIGUOUS: st.setCountAmbiguous(st.getCountAmbiguous()+1); break;
			}
			
			synsetToStatistics.put(sv.getId(), st);
			
		}
		
	    ArrayList<SynsetStatistics> stats = new ArrayList<>();
	    for (HashMap<Long, SynsetStatistics> value: cameoToSynsetStat.values()){
	    	stats.addAll(value.values());
	    }
		
	    List<SynsetStatistics> sts = ofy().load().type(SynsetStatistics.class).list();
	    
	    ofy().delete().entities(sts).now();
	    
	    ofy().save().entities(stats).now();
	}
}
