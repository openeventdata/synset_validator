package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.utd.cs.bdma.synset.validator.shared.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.Verdict;

@RemoteServiceRelativePath("validate")
public interface ValidatorService extends RemoteService{

	public SynsetEntry[] getNextForValidation(int count);
	public void submit(ArrayList<Verdict> verdicts );
}
