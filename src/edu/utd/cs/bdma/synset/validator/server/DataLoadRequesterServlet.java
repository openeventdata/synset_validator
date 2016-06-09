package edu.utd.cs.bdma.synset.validator.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class DataLoadRequesterServlet extends HttpServlet{
	
    @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    	Queue queue = QueueFactory.getDefaultQueue();
//		queue.add(TaskOptions.Builder.withUrl("/synset_validator/loaddata")
//				                     .header("Host", ModulesServiceFactory.getModulesService().getVersionHostname(null, null)));
	    
		queue.add(TaskOptions.Builder.withPayload(new DataLoaderTask()));
    }	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	
	}

}
