package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoCodeSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntrySummery;

public class CameoSummeryPanel extends PopupPanel implements HasHandlers{

	private static final CameoServiceAsync cameoService = GWT.create(CameoService.class); 
	static EventBus eventBus = GWT.create(SimpleEventBus.class);
	
	
	
	private List<CameoEntrySummery> summeries;
	private List<CameoEntrySummery> filteredSummeries = new ArrayList<>() ;
	
	private ArrayList<String> summeryTree; 
	
	private HashSet<String> clickableList = new HashSet<>();
	
	@UiField
	LoadingPanel loadingPanel;
	
	Widget parentWidget;
	
	private static CameoSummeryPanelUiBinder uiBinder = GWT.create(CameoSummeryPanelUiBinder.class);

	interface CameoSummeryPanelUiBinder extends UiBinder<Widget, CameoSummeryPanel> {
	}

	public CameoSummeryPanel() {
		setWidget(uiBinder.createAndBindUi(this));
		
			
	}

	//@UiField
	FlexTable summeryTable;
	
	//@UiField
	TextBox searchTextBox;
	
	@UiField
	VerticalPanel mainPanel;
	
	@UiField
	Tree cameoTree;
	
	
	
		
	public void getAndShow(final Widget widget){
		GWT.log("Inside TEST");
//		if (summeryTree != null)
//		            GWT.log("Num Summeries: "+summeries.size());
		parentWidget = widget;
		this.showRelativeTo(parentWidget);
//		loadingPanel.show();
		GWT.log("Inside TEST");
		if (summeryTree == null ){
			cameoService.summeryTree(new AsyncCallback<ArrayList<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					// TODO Auto-generated method stub
					summeryTree = result;
					constructTree(summeryTree);
					loadingPanel.hide();
				}
			});
		} else {
			GWT.log("Testing Drawing");
			constructTree(summeryTree);
			loadingPanel.hide();
		}
		
		
		
		
	}

	protected void constructTree(ArrayList<String> result) {
		// TODO Auto-generated method stub
		GWT.log("Inside TEST");
		ArrayList<TreeItem> rootNodes = new ArrayList<>();
		Stack<TreeItem> parent = new Stack<>();
		TreeItem prev = null;
		GWT.log("Inside TEST");
		for (String s: result){
			GWT.log("Inside TEST");
			if (!s.endsWith("*")){
				clickableList.add(s.trim().substring(0, s.trim().indexOf(":")));
			}
			if (!s.startsWith("\t"))
			{
				TreeItem item = new TreeItem(new HTML("<b>"+s+"</b>"));
				rootNodes.add(item);
				GWT.log(s);
				if (!parent.isEmpty())
					parent.pop();
				parent.push(item);
			}
			else if (!s.startsWith("\t", 1)){
				TreeItem item = new TreeItem(new HTML(s.substring(1)));
				GWT.log(s);
				parent.peek().addItem(item);
				prev = item;
			} else {
				prev.addItem(new TreeItem(new HTML(s.substring(2))));
			}
		}
		GWT.log("Size of root ndoes: "+ rootNodes.size());
		
		
		for (TreeItem it: rootNodes){
			 cameoTree.addItem(it);
			 //break;
		}
		cameoTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				// TODO Auto-generated method stub
				TreeItem item = event.getSelectedItem();
				GWT.log(clickableList.toString());
				String text = item.getText().replaceAll("<[^>]*>", ""); 
				if (clickableList.contains(text.trim().substring(0, text.indexOf(":")))){
					hide();
					fireEvent(new CameoCodeSelectedEvent(text.trim().substring(0, text.indexOf(":"))));
				}
			}
		}); 
	}
	
	
	
	

	private void constructTable(List<CameoEntrySummery> summeries2) {
		// TODO Auto-generated method stub
	    summeryTable.clear();
		int i = 0;
		summeryTable.setCellPadding(10);
		for(CameoEntrySummery summery: summeries2){
			summeryTable.setWidget(i++, 0, new HTML(buildTableEntry(summery)));
		}
		GWT.log("Finished Processing");
		//this.showRelativeTo(widget);
	}
	
	
	private String buildTableEntry(CameoEntrySummery summery){
		return "<span style=\"font-weight: bold; color: green;\">"+summery.getCode()
		             +"</span><span style=\"font-weight: bold;\"> "
		                            +summery.getConcept()+"</span> "+summery.getDescription();
	}

	//@UiHandler("summeryTable")
	void summeryClicked(ClickEvent e){
		int row = summeryTable.getCellForEvent(e).getRowIndex();
		GWT.log(""+row);
		String cameoCode = filteredSummeries.get(row).getCode();
		//eventBus.fireEvent(new CameoCodeSelectedEvent(cameoCode));
		this.hide();
		fireEvent(new CameoCodeSelectedEvent(cameoCode));
	}

	@UiHandler("hideButton")
	void handleClick(ClickEvent event){
		this.hide();
	}

    //@UiHandler("searchTextBox")
    void handleKeyPress(KeyUpEvent event){
    	
         filterResult();
    	 constructTable(filteredSummeries);
    }
    
    void filterResult(){

        filteredSummeries.clear();    
        GWT.log("Length: "+searchTextBox.getText());
		for(CameoEntrySummery summery: summeries){
			if (summery.contains(searchTextBox.getText()))
			        filteredSummeries.add(summery);
		}
		GWT.log("Filtered Summeries: "+filteredSummeries.size());
		GWT.log("Finished Processing");
		
    }

    
	

}
