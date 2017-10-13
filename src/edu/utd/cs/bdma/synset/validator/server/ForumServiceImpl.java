package edu.utd.cs.bdma.synset.validator.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.server.Authentication.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.client.ForumService;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.ForumMessage;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.Word;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ForumServiceImpl extends RemoteServiceServlet implements ForumService{


	
	static{
		ObjectifyService.register(ForumMessage.class);
		ObjectifyService.register(CameoEntry.class);
		ObjectifyService.register(Word.class);
		ObjectifyService.register(UserInfo.class);
	}
	
	@Override
	public ForumMessage add(ForumMessage message) {
		// TODO Auto-generated method stub
		UserInfo info =  ((UserInfo) this.getThreadLocalRequest().getSession().getAttribute("user"));
		message.setDate(new Date(System.currentTimeMillis()));
		message.setUserID(info.getId());
		ofy().save().entity(message).now();
		message.setFullName(info.getFirstName()+" "+info.getSecondName());
		return message;
	}

	@Override
	public ArrayList<ForumMessage> getAll(String cameoCode, String word) {
		// TODO Auto-generated method stub
		
		CameoEntry entry = ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
		Word wordEntry = ofy().load().type(Word.class).filter("text", word).first().now();
		
		if (entry != null && wordEntry != null){
			return this.list(entry.getId(), wordEntry.getId());
		}
		
		return new ArrayList<>();
		
	}

	//@Override
	public ArrayList<ForumMessage> list(Long cameoId, Long wordId) {
		// TODO Auto-generated method stub
		List<ForumMessage> messages = ofy().load().type(ForumMessage.class)
				                                  .filter("cameoID", cameoId)
				                                  .filter("wordID", wordId).list();
		
	    addTemporaryInfo(messages);
	    Collections.sort(messages, new Comparator<ForumMessage>() {

			@Override
			public int compare(ForumMessage o1, ForumMessage o2) {
				// TODO Auto-generated method stub
				return o1.getDate().compareTo(o2.getDate());
			}
		});
	    
	    return new ArrayList<>(messages);
	}

	private void addTemporaryInfo(List<ForumMessage> messages) {
		// TODO Auto-generated method stub
		List<UserInfo> users  = ofy().load().type(UserInfo.class).list();
		HashMap<Long, UserInfo> idUserMap = new HashMap<>();
		for (UserInfo info: users) idUserMap.put(info.getId(), info);
		
		for (ForumMessage msg: messages){
			UserInfo user = idUserMap.get(msg.getUserID());
			if (user != null){
				msg.setFullName(user.getFirstName()+" "+user.getSecondName());
			} else {
				msg.setFullName("Annonymous User");
			}			
		}
				
	}

	@Override
	public ForumMessage add(String message, String cameoCode, String word) {
		// TODO Auto-generated method stub
		CameoEntry entry = ofy().load().type(CameoEntry.class).filter("code", cameoCode).first().now();
		Word wordEntry = ofy().load().type(Word.class).filter("text", word).first().now();
		
		ForumMessage msg = new ForumMessage();
		msg.setCameoID(entry.getId());
		msg.setWordID(wordEntry.getId());
		msg.setMessage(message);
		
		return this.add(msg);
		

	}

}
