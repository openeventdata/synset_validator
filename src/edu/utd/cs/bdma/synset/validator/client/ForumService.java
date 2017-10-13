package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.utd.cs.bdma.synset.validator.shared.entity.ForumMessage;

@RemoteServiceRelativePath("forum")
public interface ForumService extends RemoteService{
	
      public ForumMessage add(ForumMessage message);
      
      public ForumMessage add(String message, String cameoCode, String word);
      
      public ArrayList<ForumMessage> getAll(String cameoCode, String word);
      
      
      //public ArrayList<ForumMessage> listMessages(Long cameoId, Long wordId);
      
      

}