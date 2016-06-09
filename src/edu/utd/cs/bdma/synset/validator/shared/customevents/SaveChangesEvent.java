package edu.utd.cs.bdma.synset.validator.shared.customevents;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SaveChangesEvent extends GwtEvent<SaveChangesEventHandler>{

	public static Type<SaveChangesEventHandler> TYPE = new Type<>();
	@Override
	public Type<SaveChangesEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	@Override
	protected void dispatch(SaveChangesEventHandler handler) {
		// TODO Auto-generated method stub
		GWT.log("Inside Dispatch");
		handler.onSaveChanges(this);
	}
	
	public static HandlerRegistration register(EventBus eventBus,
	        SaveChangesEventHandler handler) {
	      return eventBus.addHandler(TYPE, handler);
	    } 

}
