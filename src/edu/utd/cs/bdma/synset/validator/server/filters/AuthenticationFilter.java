package edu.utd.cs.bdma.synset.validator.server.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter{

	private ArrayList<String> excludedUrls;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getServletPath();
		System.out.println(url);
		
		if (!excludedUrls.contains(url)){
			HttpSession session = req.getSession(false);
			if (session == null){
				System.out.println("NO session Found");
			} else {
				System.out.println("Session ID"+session.getId());
			}	
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		String urls = config.getInitParameter("excluded-urls");
		StringTokenizer token = new StringTokenizer(urls, ",");
		 
        excludedUrls = new ArrayList<String>();
 
        while (token.hasMoreTokens()) {
            excludedUrls.add(token.nextToken());
 
        }
	}

}
