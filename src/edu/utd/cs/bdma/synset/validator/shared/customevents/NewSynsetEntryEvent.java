package edu.utd.cs.bdma.synset.validator.shared.customevents;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;

public class NewSynsetEntryEvent extends GwtEvent<NewSynsetEntryEventHandler>{

	public static Type<NewSynsetEntryEventHandler> TYPE = new Type<>();
	@Override
	public Type<NewSynsetEntryEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	@Override
	protected void dispatch(NewSynsetEntryEventHandler handler) {
		// TODO Auto-generated method stub
		GWT.log("Inside Dispatch");
		handler.onSave(this);
	}
	
	public static HandlerRegistration register(EventBus eventBus,
	        NewSynsetEntryEventHandler handler) {
	      return eventBus.addHandler(TYPE, handler);
	    } 

}
