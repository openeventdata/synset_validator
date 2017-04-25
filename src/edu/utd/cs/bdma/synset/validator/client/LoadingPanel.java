package edu.utd.cs.bdma.synset.validator.client;

import com.github.gwtbootstrap.client.ui.Image;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LoadingPanel extends Composite{

	HorizontalPanel panel = new HorizontalPanel();
	private String message;
	Image loadingImage = new Image("gears.gif");
	HTML messageHTML = new HTML(message);
	
	public LoadingPanel(String message) {
		// TODO Auto-generated constructor stub
		setMessage(message);
		
		panel.add(loadingImage);
		panel.add(messageHTML);
		initWidget(panel);
		panel.setVisible(false);	
	}
	
	public void setMessage(String message) {
		this.message = message;
		messageHTML.setHTML(message);
	}
	
	
	public void show(){
		
		panel.setVisible(true);
	}
	
	public LoadingPanel() {
		// TODO Auto-generated constructor stub
		this("");
	}
	
	public void hide(){
		panel.setVisible(false);
	}
}
