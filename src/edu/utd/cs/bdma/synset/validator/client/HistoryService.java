package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.utd.cs.bdma.synset.validator.shared.entity.SubmissionDetails;

@RemoteServiceRelativePath("history")
public interface HistoryService extends RemoteService{
	
	public ArrayList<SubmissionDetails> getSubmissions();
	
	

}
