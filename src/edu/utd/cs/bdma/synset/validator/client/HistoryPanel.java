package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.client.CameoSummeryPanel.CameoSummeryPanelUiBinder;
import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoCodeSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoWordSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.entity.SubmissionDetails;

public class HistoryPanel extends PopupPanel{

	private static HistoryPanelUiBinder uiBinder = GWT.create(HistoryPanelUiBinder.class);

	private static final HistoryServiceAsync historyService = GWT.create(HistoryService.class);
	
	interface HistoryPanelUiBinder extends UiBinder<Widget, HistoryPanel> {
	}

static EventBus eventBus = GWT.create(SimpleEventBus.class);
	
	private HandlerManager handlerManager;

	private String searchKey = "";
	
	private List<String> summeries;
	
	


	public HistoryPanel() {
		setWidget(uiBinder.createAndBindUi(this));
		handlerManager = new HandlerManager(this);
			
	}

	@UiField
	FlexTable historyTable;
	
	@UiField
	TextBox searchTextBox;

	private ArrayList<SubmissionDetails> submitDetails;
	
		
	public void getAndShow(final Widget widget){
		
			historyService.getSubmissions(new AsyncCallback<ArrayList<SubmissionDetails>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<SubmissionDetails> result) {
					// TODO Auto-generated method stub
					submitDetails = result;
					constructTable(submitDetails, widget);
				}
			});
		
	}

	private void constructTable(ArrayList<SubmissionDetails> submitDetails2, Widget widget) {
		// TODO Auto-generated method stub
	    historyTable.clear();
		int i = 0;
		historyTable.setCellPadding(10);
		for(SubmissionDetails sd: submitDetails2){
			historyTable.setWidget(i++, 0, new HTML(constructHtml(sd)));
		}
		GWT.log("Finished Processing");
		this.showRelativeTo(widget);
	}
	
	private String constructHtml(SubmissionDetails sd){
		StringBuilder sb = new StringBuilder();
		sb.append("<b>"+sd.getCameoCode()+"</b>: ");
		sb.append(sd.getCameoDef()+"<br/>");
		sb.append(sd.getCameoDescription()+"<br/>");
		sb.append("<span style=\"color: green;\"><b>Word</b>: "+sd.getWord()+"</span>");
		
		return sb.toString();
		
	}

	
	@UiHandler("hideButton")
	void handleClick(ClickEvent event){
		this.hide();
	}

    @UiHandler("searchTextBox")
    void handleKeyPress(KeyUpEvent event){
    	historyTable.clear();
		int i = 0;
		historyTable.setCellPadding(10);
		for(SubmissionDetails sd: submitDetails){
			if (sd.contains(searchTextBox.getText()))
			  historyTable.setWidget(i++, 0, new HTML(constructHtml(sd)));
		}
		GWT.log("Finished Processing");

    	
    }
    
    @UiHandler("historyTable")
	void onSubmissionSelected(ClickEvent e){
		int row = historyTable.getCellForEvent(e).getRowIndex();
		GWT.log(""+row);
		//eventBus.fireEvent(new CameoCodeSelectedEvent(cameoCode));
		this.hide();
		//fireEvent(new CameoCodeSelectedEvent(submitDetails.get(row).getCameoCode()));
		fireEvent(new CameoWordSelectedEvent(submitDetails.get(row).getCameoCode(), submitDetails.get(row).getWord()));
	}

}
