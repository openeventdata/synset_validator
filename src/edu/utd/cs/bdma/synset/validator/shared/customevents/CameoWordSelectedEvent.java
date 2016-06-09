package edu.utd.cs.bdma.synset.validator.shared.customevents;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;

public class CameoWordSelectedEvent extends GwtEvent<CameoWordSelectedEventHandler>{
	
	
	
	
	public static Type<CameoWordSelectedEventHandler> TYPE = new Type<>();
	private String code;
	private String word;
	
	@Override
	public Type<CameoWordSelectedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	public CameoWordSelectedEvent(String code, String word) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.word = word;
		GWT.log("Code Set");
	}

	@Override
	protected void dispatch(CameoWordSelectedEventHandler handler) {
		// TODO Auto-generated method stub
		GWT.log("Inside Dispatch");
		handler.onWordReceived(this);
	}
	
	public String getCode() {
		return code;
	}
	
	public static HandlerRegistration register(EventBus eventBus,
	        CameoWordSelectedEventHandler handler) {
	      return eventBus.addHandler(TYPE, handler);
	    }

	public String getWord() {
		return word;
	}


}
