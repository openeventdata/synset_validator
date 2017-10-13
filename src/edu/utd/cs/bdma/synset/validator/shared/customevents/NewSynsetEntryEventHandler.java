package edu.utd.cs.bdma.synset.validator.shared.customevents;

import com.google.gwt.event.shared.EventHandler;

public interface NewSynsetEntryEventHandler extends EventHandler{
	
	public void onSave(NewSynsetEntryEvent event);

}