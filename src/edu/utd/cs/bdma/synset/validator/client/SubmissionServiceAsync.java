package edu.utd.cs.bdma.synset.validator.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.utd.cs.bdma.synset.validator.shared.entity.UpdatedInfo;

public interface SubmissionServiceAsync {

	void submit(UpdatedInfo info, String cameoCode, String word, AsyncCallback<Void> callback);

	void retrieve(Long submissionId, AsyncCallback<UpdatedInfo> callback);

	void retrieve(String cameoCode, String word, AsyncCallback<UpdatedInfo> callback);

}
