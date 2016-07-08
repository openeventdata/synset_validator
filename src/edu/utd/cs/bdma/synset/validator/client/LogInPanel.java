package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.customevents.LoginSuccessEvent;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;

public class LogInPanel extends PopupPanel {

	private final static UserInfoServiceAsync userService = GWT.create(UserInfoService.class);

	private Storage localDB = null;

	interface LogInPanelUiBinder extends UiBinder<Widget, LogInPanel> {
	}

	private static LogInPanelUiBinder uiBinder = GWT.create(LogInPanelUiBinder.class);

	@UiField
	Button forgetPasswordButton;

	@UiField
	SubmitButton sendRecInfoButton;

	@UiField
	PasswordTextBox passwordTextBox;

	@UiField
	PasswordTextBox passwordTextBoxSignUp;

	@UiField
	TextBox emailTextBox;

	@UiField
	CheckBox remMeCheckbox;

	@UiField
	SubmitButton loginButton;

	@UiField
	ControlGroup passwordFields;

	@UiField
	Label errorLabel;

	@UiField
	TextBox firstNameTextBoxSignUp;

	@UiField
	TextBox lastNameTextBoxSignUp;

	@UiField
	TextBox emailTextBoxSignUp;

	@UiField
	ListBox langListbox;

	@UiField
	Form signUpForm;

	@UiField
	Form verifyForm;
	@UiField
	TextBox verifyCodeTextBox;

	@UiField
	TabPanel tabPanel;

	@UiField
	ListBox countryListBox;
	
	@UiField
	HTML verifyEmailLabel;
	
	@UiField
	Form newPasswordForm;
	
	@UiField
	ControlGroup emailGroup;
	
	@UiField
	TextBox verifyPassRecCodeTB;
	
	@UiField
	TextBox newPasswordTB;
	
	@UiField
	TextBox newPasswordTB2;
	
	

	String userFullName;

	public LogInPanel() {
		setWidget(uiBinder.createAndBindUi(this));
		localDB = Storage.getLocalStorageIfSupported();

	}

	public void checkLogin() {
		userService.checkLogin(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if (result == null) {
					center();
				} else {
					userFullName = result.split(",")[0];
					localDB.setItem("country", result.split(",")[1]);
					notifySuccess();
				}
			}
		});
	}

	@UiHandler("forgetPasswordButton")
	void onClick(ClickEvent e) {
		remMeCheckbox.setVisible(false);
		passwordFields.setVisible(false);
		loginButton.setVisible(false);
		sendRecInfoButton.setVisible(true);
	}
	
	@UiHandler("sendRecInfoButton")
	void onPasswordRecRequest(ClickEvent e){
		String emailAddress = emailTextBox.getText();
		userService.sendRecoveryRequest(emailAddress, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				sendRecInfoButton.setVisible(false);
				emailGroup.setVisible(false);
				newPasswordForm.setVisible(true);
			}
		});
	}
	
	@UiHandler("changePassButton")//add handler, make button
	void verifyRecovery(ClickEvent e){
		//get email
		String email = emailTextBox.getText();
		String verificationCode = verifyPassRecCodeTB.getText();//get code;
		String password1 = newPasswordTB.getText();//get pass;
		String password2 = newPasswordTB2.getText();//get pass;
		if(password1.equals(password2)){
			userService.verifyPassword(email, verificationCode, password1, new AsyncCallback<Boolean>(){
				
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				if(result){
					newPasswordForm.setVisible(false);
					remMeCheckbox.setVisible(true);
					passwordFields.setVisible(true);
					loginButton.setVisible(true);
					errorLabel.setText("Password reset successfully");
					newPasswordForm.setVisible(false);
				}
				else{
					errorLabel.setText("Incorrect verification code;");
				}
			}
			});
		}
		else{
			errorLabel.setText("Passwords do not match.");
			}
	}

	@UiHandler("loginButton")
	void onClickLoginButton(ClickEvent e) {
		String email = emailTextBox.getText().trim();
		String password = passwordTextBox.getText();
		userService.login(email, password, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				errorLabel.setText(result);
				if (result.contains("success")) {
					hide();
					userFullName = result.substring(result.indexOf("#") + 1).split(",")[0];
					localDB.setItem("country", result.split(",")[1]);
					notifySuccess();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

	}

	void notifySuccess() {
		fireEvent(new LoginSuccessEvent());
	}

	public void logout() {
		userService.logout(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				passwordTextBox.setText("");
				errorLabel.setText("");
				center();
			}
		});

	}

	public String getUserFullName() {
		return userFullName;
	}

	@UiHandler("signUpButton")
	void clickSignUp(ClickEvent e) {
		UserInfo info = new UserInfo(emailTextBoxSignUp.getText().trim(), passwordTextBoxSignUp.getText(),
				firstNameTextBoxSignUp.getText().trim(), lastNameTextBoxSignUp.getText().trim(),
				langListbox.getSelectedItemText());
		info.setCountry(countryListBox.getSelectedItemText());
		userService.signup(info, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				if (result) {
					GWT.log("Success");
					signUpForm.setVisible(false);
					verifyForm.setVisible(true);
					verifyEmailLabel.setHTML("<b>"+emailTextBoxSignUp.getText()+"</b>");
				} else {
					GWT.log("Failed");
				}
			}
		});
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		userService.listCountries(new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
				for (String country : result) {
					countryListBox.addItem(country);
					sb.append(country + ",");
				}

				localDB.setItem("country_list", sb.deleteCharAt(sb.length() - 1).toString());

			}
		});
	}

	@UiHandler("verifyButton")
	void verifyButton(ClickEvent e) {
		userService.verify(emailTextBoxSignUp.getText(), verifyCodeTextBox.getText(), new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				if (result) {
					GWT.log("Hiding Window");
					errorLabel.setText("User registered successfully.");
					verifyForm.setVisible(false);
					signUpForm.setVisible(true);
					tabPanel.selectTab(0);
					emailTextBox.setText(emailTextBoxSignUp.getText());
					passwordTextBox.setText("");
				} else {
					GWT.log("Failed");
				}
			}
		});
	}

}
