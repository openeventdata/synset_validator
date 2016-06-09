package edu.utd.cs.bdma.synset.validator.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.utd.cs.bdma.synset.validator.shared.entity.UpdatedInfo;

@RemoteServiceRelativePath("submit")
public interface SubmissionService extends RemoteService{
	
	public void submit(UpdatedInfo info, String cameoCode, String word);
	
	public UpdatedInfo retrieve(Long submissionId);
	
	
    public UpdatedInfo retrieve(String cameoCode, String word);

}
