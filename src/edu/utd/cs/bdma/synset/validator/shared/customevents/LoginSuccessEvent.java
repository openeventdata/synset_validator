package edu.utd.cs.bdma.synset.validator.shared.customevents;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LoginSuccessEvent extends GwtEvent<LoginSuceessEventHandler>{

	 public static Type<LoginSuceessEventHandler> TYPE = new Type<>();
	@Override
	public Type<LoginSuceessEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	
	

	@Override
	protected void dispatch(LoginSuceessEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onLoginSucceed(this);
	}

}
