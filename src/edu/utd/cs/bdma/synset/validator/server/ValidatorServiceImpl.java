package edu.utd.cs.bdma.synset.validator.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.persistence.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.client.ValidatorService;
import edu.utd.cs.bdma.synset.validator.shared.Synset;
import edu.utd.cs.bdma.synset.validator.shared.SynsetDeserializer;
import edu.utd.cs.bdma.synset.validator.shared.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.Verdict;
import static com.googlecode.objectify.ObjectifyService.ofy;
public class ValidatorServiceImpl extends RemoteServiceServlet implements ValidatorService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SynsetEntry[] entries;
	
	
	int i;
	
	static{
		ObjectifyService.register(Verdict.class);
	}
	
	
	public ValidatorServiceImpl() {
		// TODO Auto-generated constructor stub
		
		
		Gson gson = new Gson();
		
		
		try {
            String jsonString = getContentAsString("Output2.json");			
			entries = gson.fromJson(jsonString, SynsetEntry[].class);
			
			
		} catch (JsonSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if (entries == null){
			log("Failed to load data");
		}
	}
	
	private String getContentAsString(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		StringBuilder sb = new StringBuilder();
		while (br.ready()){
			sb.append(br.readLine());
		}
		br.close();
		
		return sb.toString();
	}
	@Override
	public SynsetEntry[] getNextForValidation(int count) {
		// TODO Auto-generated method stub
		
		SynsetEntry[] entryArray = new SynsetEntry[count];
		
		entryArray[0] = entries[i++%entries.length];
		log("Sending Data to client");
		log(entryArray[0].getConcept());
		log(""+(entryArray[0].getSets()[0]==null));
		return entryArray;
	}

	@Override
	public void submit(ArrayList<Verdict> verdicts) {
		// TODO Auto-generated method stub
		ofy().save().entities(verdicts).now();
	
	}

	
}
