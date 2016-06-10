package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;

@RemoteServiceRelativePath("userinfo")
public interface UserInfoService extends RemoteService{
	
	public static final String ERROR_NO_USER = "user not registered";
	public static final String ERROR_PASSWORD_MISMATCH = "incorrect password";
	public static final String SUCCESS_PREFIX = "success";
	public static final String ERROR_USER_ALREADY_EXISTS = "email address already in use";
	public static final String ERROR_USER_NOT_VERFIED = "User not verified";

	public String login(String email, String password);
	
	public boolean signup(UserInfo info);
	
	public boolean verify(String email, String code);
	
	public boolean verifyPassword(String email, String code, String password);
	
	public boolean logout();
	
	public String checkLogin();
	
	public ArrayList<String> listCountries();
	
	public void sendRecoveryRequest(String emailAddress);
}
