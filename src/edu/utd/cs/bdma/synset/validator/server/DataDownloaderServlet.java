package edu.utd.cs.bdma.synset.validator.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DataDownloaderServlet extends HttpServlet{
	
	static{
		ObjectifyService.register(SynsetWord.class);
		ObjectifyService.register(CameoRule.class);
		
		
	}
	
	Gson gson = new Gson();
	
	String packageName = "edu.utd.cs.bdma.synset.validator.shared.entity";
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		
		String className = req.getParameter("class");
		resp.setCharacterEncoding("UTF-8");
		if (className == null || className.length() == 0){
			resp.getOutputStream().write("No Classname".getBytes());
			resp.getOutputStream().flush();
		} else {
			try{
				Class cls = Class.forName(packageName+"."+className);
				ObjectifyService.register(cls);
				List objectsList = ofy().load().type(cls).list();
				
				String output = gson.toJson(objectsList);
				byte[] utf8JsonString = output.getBytes("UTF8");
				resp.setContentType("application/json");
				resp.getOutputStream().write(utf8JsonString);
				resp.getOutputStream().flush();
				
			} catch (Exception ex){
				resp.getOutputStream().write("Class Not Found".getBytes());
				resp.getOutputStream().flush();
			}
		}
		
	}
	
//	
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		this.doPost(req, resp);
//	}

}
