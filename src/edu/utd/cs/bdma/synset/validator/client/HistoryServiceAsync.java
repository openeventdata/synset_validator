package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.utd.cs.bdma.synset.validator.shared.entity.SubmissionDetails;


public interface HistoryServiceAsync {

	void getSubmissions(AsyncCallback<ArrayList<SubmissionDetails>> callback);
	

}
