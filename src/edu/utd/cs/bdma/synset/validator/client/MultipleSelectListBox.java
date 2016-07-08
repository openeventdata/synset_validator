package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.HashSet;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.IconPosition;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.core.java.util.Arrays;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MultipleSelectListBox extends Composite{
	
	private ArrayList<String> options  = new ArrayList<>();
	private ArrayList<Boolean> selections = new ArrayList<>();
	private Button button;
	private int length;
	private int height = 150;
	private PopupPanel popup;
	
	public MultipleSelectListBox(String name, int dynNameLength) {
		// TODO Auto-generated constructor stub
		button = new Button(name);
		button.setIcon(IconType.LIST);
		button.setIconPosition(IconPosition.RIGHT);
		initWidget(button);
		length = dynNameLength;
		popup = new PopupPanel();
        button.setWidth("180px");   
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!popup.isShowing()){
				    showOptions();
				}
			}
		});
		
		
	}
	
	
	public MultipleSelectListBox(String name) {
		// TODO Auto-generated constructor stub
		this(name, 0);
	}
	
	
	private void updateName(){
		if (length == 0){
			//do nothing
		} else {
			String str = getSelectedItemsAsStr(",");
			if (str.length() > 0){
				String str2 = str.substring(0, length);
				if (str2.length() == str.length()){
					button.setText(str);
				} else {
					str2 = str2.substring(0, str2.length()-3)+"...";
					button.setText(str2);
				}
			}
		}
	}
	
	
	protected void showOptions() {
		// TODO Auto-generated method stub
		popup.clear();
		ScrollPanel sPanel = new ScrollPanel();
		sPanel.setAlwaysShowScrollBars(false);
		sPanel.setHeight(height+"px");
		VerticalPanel panel = new VerticalPanel();
		final FlexTable table = new FlexTable();
		table.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int row = table.getCellForEvent(event).getRowIndex();
				selections.set(row, !selections.get(row));
				if (selections.get(row)){
					table.getRowFormatter().addStyleName(row, "listSelectedItem");
				} else {
					table.getRowFormatter().removeStyleName(row, "listSelectedItem");
				}
				updateName();
			}
		});
		for (int i = 0; i < options.size(); i++){
			
			if (selections.get(i)){
				table.getRowFormatter().addStyleName(i, "listSelectedItem");
			} else {
				table.getRowFormatter().removeStyleName(i, "listSelectedItem");
			}
			table.setWidget(i, 0, new HTML(SafeHtmlUtils.htmlEscape(options.get(i))));
		}
		Button saveButton = new Button("Save");
		sPanel.add(table);
		panel.add(sPanel);
		panel.add(saveButton);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				popup.hide();
				
			}
		});

		popup.add(panel);
		popup.showRelativeTo(button);
		
	}

	protected void updateSelections() {
		// TODO Auto-generated method stub
		
	}

	public void addOption(String option){
		options.add(option);
		selections.add(false);
		
	}
	
	public void addOptions(ArrayList<String> options){
		//this.options.addAll(options);
		for (String o: options){
			addOption(o);
		}
	}
	
	
	public ArrayList<String> getSelectedItemsAsList(){
		ArrayList<String> selList = new ArrayList<>();
		for (int i = 0; i < selections.size(); i++){
			if (selections.get(i))selList.add(options.get(i));
		}
		return selList;
	}
	
	public String getSelectedItemsAsStr(String delim){
		ArrayList<String> selList = getSelectedItemsAsList();
		StringBuilder sb = new StringBuilder();
		for (String sel : selList){
			sb.append(sel+delim);
		}
		if (sb.length() > 0){
			return sb.toString().substring(0, sb.length()-delim.length());
		} else {
			return "";
		}	
	}
	
	public void setDefaultSelections(String str, String delim){
		String[] parts = str.split(delim);
		HashSet<String> set = new HashSet<>(java.util.Arrays.asList(parts));
		for (int i = 0; i < options.size(); i++){
			selections.set(i, set.contains(options.get(i)));
			
		}
		updateName();
	}
	
	
	
	
	
	
	
	

}
