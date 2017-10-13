package edu.utd.cs.bdma.synset.validator.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.utd.cs.bdma.synset.validator.shared.entity.ForumMessage;

public class ForumMessageWidget extends Composite{
	
	private static DateTimeFormat formatter = DateTimeFormat.getFormat("MMMM dd, yyyy HH:MM a");
	
	VerticalPanel vPanel;
	ForumMessage message; 
	public ForumMessageWidget(ForumMessage msg) {
		// TODO Auto-generated constructor stub
		vPanel =  new VerticalPanel();
		message = msg;
		initWidget(vPanel);
		
		design();
	}
	
	private void design(){
		String dateStr = formatter.format(message.getDate());
		HTML username = new HTML("<h5 style=\"display: inline;\">"+message.getFullName()+"</h5>"+"<span style=\"color: green; float: right;\">"+dateStr+"</span>");
		HTML messageLabel = new HTML("<div style=\"background: #f9f9eb; width: 100%; padding: 4px;\">"+message.getMessage()+"</div>");
		vPanel.add(username);
		vPanel.setWidth("100%");	
		vPanel.add(messageLabel);
	}
	

}
