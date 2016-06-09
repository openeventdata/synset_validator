package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.utd.cs.bdma.synset.validator.shared.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.Verdict;

public interface ValidatorServiceAsync {

	void getNextForValidation(int count, AsyncCallback<SynsetEntry[]> callback);

	void submit(ArrayList<Verdict> verdicts, AsyncCallback<Void> callback);

}
