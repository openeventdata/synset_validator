package edu.utd.cs.bdma.synset.validator.client;

import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PopupLabel extends Composite{
	
	HTML label;
	String labelText;
	String editedLT;
	int verdict;
	Long id;
	
	public PopupLabel(String lt, int verdict, Long id) {
		// TODO Auto-generated constructor stub
		this.labelText = lt;
		this.editedLT = labelText;
		label = new HTML(editedLT);
		this.verdict = verdict;
		this.id = id;
		
		initWidget(label);
		
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showPopup();
			}
		});
		
	}


	protected void showPopup() {
		// TODO Auto-generated method stub
		final PopupPanel panel = new PopupPanel();
		
		final VerticalPanel vPanel = new VerticalPanel();
		final TextArea ta = new TextArea();
		ta.setText(editedLT);
		ta.setEnabled(false);
		RadioButton correctRadio = new RadioButton("G"+id, "Correct");
		correctRadio.setValue(verdict == 0);
		RadioButton incrrectRadio = new RadioButton("G"+id, "Incorrect");
		incrrectRadio.setValue(verdict == 1);
		RadioButton ambiguousRadio = new RadioButton("G"+id, "Ambiguous");
		ambiguousRadio.setValue(verdict == 2);
		Button doneButton = new Button("Done");
		
		doneButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				for (int i = 2; i < 5; i++){
					if (((RadioButton)vPanel.getWidget(i)).getValue()){
						verdict = i-2;
					}
				}
				editedLT = ta.getText();
				label.setHTML(editedLT);
				panel.hide();
			}
			
		});
		
		vPanel.add(ta);
		vPanel.add(new Label("The example is - "));
		vPanel.add(correctRadio);
		vPanel.add(incrrectRadio);
		vPanel.add(ambiguousRadio);
		vPanel.add(doneButton);
		
		panel.add(vPanel);
		
		panel.showRelativeTo(this.label);
		
		
	}
	
	public int getVerdict() {
		return verdict;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getEditedLT() {
		return editedLT;
	}
	
	boolean isTextUpdated(){
		return !editedLT.equals(labelText);
	}
	
	

}
