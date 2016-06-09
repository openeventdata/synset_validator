package edu.utd.cs.bdma.synset.validator.shared.customevents;

import com.google.gwt.event.shared.EventHandler;

public interface LoginSuceessEventHandler extends EventHandler{
	void onLoginSucceed(LoginSuccessEvent event);
}
