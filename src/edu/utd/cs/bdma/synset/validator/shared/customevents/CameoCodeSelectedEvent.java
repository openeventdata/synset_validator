package edu.utd.cs.bdma.synset.validator.shared.customevents;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class CameoCodeSelectedEvent extends GwtEvent<CameoCodeSelectedEventHandler>{

	public static Type<CameoCodeSelectedEventHandler> TYPE = new Type<>();
	private String code;
	@Override
	public Type<CameoCodeSelectedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	public CameoCodeSelectedEvent(String code) {
		// TODO Auto-generated constructor stub
		this.code = code;
		GWT.log("Code Set");
	}

	@Override
	protected void dispatch(CameoCodeSelectedEventHandler handler) {
		// TODO Auto-generated method stub
		GWT.log("Inside Dispatch");
		handler.onCodeReceived(this);
	}
	
	public String getCode() {
		return code;
	}
	
	public static HandlerRegistration register(EventBus eventBus,
	        CameoCodeSelectedEventHandler handler) {
	      return eventBus.addHandler(TYPE, handler);
	    } 

}
