package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;

public interface UserInfoServiceAsync {

	void login(String email, String password, AsyncCallback<String> callback);

	void signup(UserInfo info, AsyncCallback<Boolean> callback);

	void verify(String email, String code, AsyncCallback<Boolean> callback);

	void logout(AsyncCallback<Boolean> callback);

	void checkLogin(AsyncCallback<String> callback);

	void listCountries(AsyncCallback<ArrayList<String>> callback);
	
	

}
