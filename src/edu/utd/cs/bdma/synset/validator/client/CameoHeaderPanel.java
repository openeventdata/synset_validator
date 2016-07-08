package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoWordSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.customevents.SaveChangesEvent;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;

public class CameoHeaderPanel extends Composite  {

	private static CameoHeaderPanelUiBinder uiBinder = GWT.create(CameoHeaderPanelUiBinder.class);

	private static final CameoServiceAsync cameoService = GWT.create(CameoService.class); 
	
	
	private String cameoCode;
	
	interface CameoHeaderPanelUiBinder extends UiBinder<Widget, CameoHeaderPanel> {
	}

	public String getCameoCode() {
		return cameoCode;
	}

	@UiField
	Heading heading;
	
	@UiField
	Label summeryLabel;
	
	@UiField
	Button detailsButton;
	
	@UiField
	ListBox wordsListBox;
	
	@UiField
	HorizontalPanel horizontalPanel;
	
	@UiField
	Button saveButton;
	
	@UiField
	Button loadButton;
	
	@UiField
	CheckBox autoAdvance;
	
	CameoPopUpPanel cameoPopUpPanel = new CameoPopUpPanel();
	
	
	CameoEntry entry;

	public CameoHeaderPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		

	}
	
	public void setCode(final String cameoCode, final String defaultSelected){
		GWT.log("Inside Set()");
		if (this.cameoCode == null || !this.cameoCode.equals(cameoCode)){
			GWT.log("Insode IF");
			this.cameoCode = cameoCode;
			cameoPopUpPanel.setVisible(false);
			wordsListBox.setVisible(false);
			cameoService.getCameoInfo(cameoCode, new AsyncCallback<CameoEntry>() {
				
				@Override
				public void onSuccess(CameoEntry result) {
					// TODO Auto-generated method stub
					entry = result;

					prepareGUI();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					GWT.log("Failure fetching cameo info");
				}
			});
			
			cameoService.getRelatedWords(cameoCode, new AsyncCallback<ArrayList<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					// TODO Auto-generated method stub
					GWT.log("Sucesss");
					wordsListBox.clear();
					for (String s: result){
						wordsListBox.addItem(s);
					}
					wordsListBox.setVisible(true);
					if (defaultSelected != null) {
						fireEvent(new CameoWordSelectedEvent(cameoCode, defaultSelected));
						setSelection(defaultSelected);
					}

					
				}
			});
			
		}
		
		else if (defaultSelected != null) {
			fireEvent(new CameoWordSelectedEvent(cameoCode, defaultSelected));
			setSelection(defaultSelected);
		}


	}
	
	public boolean advanceToNextWord(){
		if (autoAdvance.getValue()){
			if (wordsListBox.getSelectedIndex() < wordsListBox.getItemCount()-1){
				wordsListBox.setSelectedIndex(wordsListBox.getSelectedIndex()+1);
				fireEvent(new CameoWordSelectedEvent(cameoCode, wordsListBox.getSelectedItemText()));
			}
		}
		
		return autoAdvance.getValue();
	}
	
	public void setCode(String cameoCode){
		setCode(cameoCode, null);
	}
	
	
	public void setSelection(String word){
		int selectedIndex = 0;
		for (int i = 0; i < wordsListBox.getItemCount(); i++){
			if (wordsListBox.getItemText(i).equals(word)){
				selectedIndex = i;
				saveButton.setActive(true);
				break;
				
			}
		}
		wordsListBox.setSelectedIndex(selectedIndex);
	}
	
	@UiHandler("loadButton")
	public void onLoadRequest(ClickEvent e)
	{
		fireEvent(new CameoWordSelectedEvent(cameoCode, wordsListBox.getSelectedItemText()));
		saveButton.setEnabled(true);
	}
	
	@UiHandler("saveButton")
	public void onSaveRequest(ClickEvent e){
		GWT.log("Save Requested");
		fireEvent(new SaveChangesEvent());
	}

	protected void prepareGUI() {
		// TODO Auto-generated method stub
        horizontalPanel.getElement().getStyle().setPadding(10, Unit.PX); 
		detailsButton.addStyleDependentName("paddedLeft");
		
		heading.setText(entry.getCode()+" "+entry.getConcept());
		summeryLabel.setText(entry.getDescription());
		this.setVisible(true);
	}

	
	
	@UiHandler("detailsButton")
	void onClick(ClickEvent e) {
		cameoPopUpPanel.setCemeoEntry(entry);
		cameoPopUpPanel.getAndShow(entry.getCode(), detailsButton);
	}

	

}
