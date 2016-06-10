package edu.utd.cs.bdma.synset.validator.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.cache.CachingDatastoreServiceFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import edu.utd.cs.bdma.synset.validator.client.UserInfoService;
import edu.utd.cs.bdma.synset.validator.shared.entity.UserInfo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;

public class UserInfoServiceImpl extends RemoteServiceServlet implements UserInfoService {

	static DatastoreService dataCache = CachingDatastoreServiceFactory.getDatastoreService(900);
	static String appId = SystemProperty.applicationId.get();
	static String verifyAccMsg = "Please enter the follwoing code in the sign-up window to verify your identity.\n\n CODE:  ";
	static String passwordRecMsg = "Please enter the following code to reset your password. \n\nPassword Recovery Code: ";
	static {
		ObjectifyService.register(UserInfo.class);
	}
	
	
	static ArrayList<String> countries = new ArrayList<>();
	
	static {
		try {
			BufferedReader br = new BufferedReader(new FileReader("countries.txt"));
			while (br.ready()){
				countries.add(br.readLine().split("\t")[0].trim());
			}
			Collections.sort(countries);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String login(String email, String password) {
		// TODO Auto-generated method stub
		UserInfo user = ofy().load().type(UserInfo.class).filter("emailAddress", email).first().now();
		log(email);

		if (user == null) {
			return ERROR_NO_USER;
		} else if (!user.getEncryptedPassword().equals(encrypt(password))) {
			return ERROR_PASSWORD_MISMATCH;
			
		} else if (!user.isVerified()) {
			return ERROR_USER_NOT_VERFIED;
		}
		else {
			HttpServletRequest request = this.getThreadLocalRequest();
			request.getSession(true).setAttribute("user", user);
			
			return SUCCESS_PREFIX + "#" + user.getFirstName()+" "+user.getSecondName()+","+user.getCountry();
			
		}
	}

	@Override
	public boolean signup(UserInfo info) {
		// TODO Auto-generated method stub
		log(info.getEmailAddress());
		info.setEncryptedPassword(encrypt(info.getEncryptedPassword()));
		UserInfo storedInfo = ofy().load().type(UserInfo.class).filter("emailAddress", info.getEmailAddress()).first()
				.now();
		if (storedInfo == null) {
			ofy().save().entity(info).now();

			sendVerificationEmail(info.getEmailAddress(), verifyAccMsg, "VerificationInfo");
			return true;
		}
		return false;
	}
	
//	private ClientResponse sendMailgunVerificationMail(String emailAddress){
//	
//		       Client client = Client.create();
//		       client.addFilter(new HTTPBasicAuthFilter("api",
//		                       "bb12f610cb9c527bb3472cb5f125f7de"));
//		       WebResource webResource =
//		               client.resource("https://api.mailgun.net/v3/YOUR_DOMAIN_NAME" +
//		                               "/messages");
//		       MultivaluedMapImpl formData = new MultivaluedMapImpl();
//		       formData.add("from", "Excited User <mailgun@YOUR_DOMAIN_NAME>");
//		       formData.add("to", emailAddress);
//		       formData.add("to", "admin@Sandboxa974f1e59ef24dde96fbbaf0ac48415e.Mailgun.Org");
//		       formData.add("subject", "Verification Required");
//		       formData.add("text", "Testing some Mailgun awesomness!");
//		       return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
//		               post(ClientResponse.class, formData);
//		
//	}

	private void sendVerificationEmail(String emailAddress, String message, String kind) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = message;

		String code = generateCode(8);
		msgBody += code;
		
		Entity entity = new Entity(kind);
		entity.setProperty("email", emailAddress);
		entity.setProperty("code", code);
		dataCache.put(entity);


		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("admin@"+appId+".appspotmail.com", "Admin"));
			msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailAddress, ""));
			msg.setSubject("Verification Required");
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String generateCode(int numChars) {
		SecureRandom random = new SecureRandom();
		String code =  new BigInteger(numChars * 8, random).toString(32);
		log("Code: "+code);
		return code;

	}

	@Override
	public boolean verify(final String email, String code) {
		// TODO Auto-generated method stub
		Query query = new Query("VerificationInfo").setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email));
		PreparedQuery pquery = dataCache.prepare(query);
		Iterable<Entity> iter = pquery.asIterable();
		if (iter.iterator().hasNext()){
			boolean result = iter.iterator().next().getProperty("code").equals(code);
			UserInfo info = ofy().load().type(UserInfo.class).filter("emailAddress", email).first().now();
            info.setVerified(result);
            ofy().save().entity(info).now();
			
			return result;
		}
		else return false; 
	}

	@Override
	public boolean logout() {
		// TODO Auto-generated method stub
		this.getThreadLocalRequest().getSession().invalidate();
		return true;
		
	}

	@Override
	public String checkLogin() {
		// TODO Auto-generated method stub
		if (this.getThreadLocalRequest().getSession(false) == null){
			return null;
		}
		UserInfo user = (UserInfo) this.getThreadLocalRequest().getSession(false).getAttribute("user");
		return user.getFirstName()+" "+user.getSecondName()+","+user.getCountry();
	}

	@Override
	public ArrayList<String> listCountries() {
		// TODO Auto-generated method stub
		return countries;
	}
	
	private String encrypt(String plaintext){
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] hash = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
		return new String(hash);
	}

	@Override
	public void sendRecoveryRequest(String emailAddress) {
		// TODO Auto-generated method stub
		log("Sending Password Rec");
		UserInfo info = ofy().load().type(UserInfo.class).filter("emailAddress", emailAddress).first().now();
		if (info == null){
			//do nothing
		}
		
		else {
			sendPasswordRecoveryInfo(emailAddress);
		}
				
				
				
	}
	
	private void sendPasswordRecoveryInfo(String email){
		sendVerificationEmail(email, passwordRecMsg, "PasswordVerificationInfo");
	}

}
