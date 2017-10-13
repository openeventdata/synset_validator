package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.utd.cs.bdma.synset.validator.shared.entity.ForumMessage;

public interface ForumServiceAsync {

	//void listMessages(Long cameoId, Long wordId, AsyncCallback<ArrayList<ForumMessage>> callback);

	void getAll(String cameoCode, String word, AsyncCallback<ArrayList<ForumMessage>> asyncCallback);

	void add(ForumMessage message, AsyncCallback<ForumMessage> callback);

	void add(String message, String cameoCode, String word, AsyncCallback<ForumMessage> callback);


}
