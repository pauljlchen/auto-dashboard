package org.xbot.core.system;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

public class SystemLanguageFilter implements Filter {
	
//	@Resource(name="localeResolver") 
//	private CookieLocaleResolver localeResolver;
		/** LocaleResolver used by this servlet */
	    private LocaleResolver localeResolver;
		
	  @Override
	  public void init(FilterConfig filterConfig) throws ServletException {

	  }

	  @Override
	  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	   
	    String changeLanguage = httpRequest.getParameter("changeLanguage");
	    HttpServletResponse httpResponse = (HttpServletResponse) response; 
	    Locale temp = null;
	    
	    
	    if (changeLanguage != null && changeLanguage.length() > 0){
	    	//want to change locale activately
	    	
		    //user and password is valid, set the local
	    	
	    	
			if("zh".equals(changeLanguage)){
				temp = new Locale("zh", "CN"); 
	            
	            Cookie c = new Cookie("cookieLanguage", "zh");
	            c.setMaxAge(1000000);
	            httpResponse.addCookie(c);
	            (new CookieLocaleResolver()).setLocale(httpRequest, httpResponse, temp); 
	        } else if("en".equals(changeLanguage)){
	        	temp = new Locale("en", "US"); 
	           
	            Cookie c = new Cookie("cookieLanguage", "en");
	            c.setMaxAge(1000000);
	            httpResponse.addCookie(c);
	            (new CookieLocaleResolver()).setLocale(httpRequest, httpResponse, temp);
	        }
	     } else {
	        	
	    	//read the cookie
	    	Cookie[] cookies = httpRequest.getCookies();//这样便可以获取一个cookie数组
	    	String cookieLanguage = null;

	    	if (cookies != null){
	    		for(Cookie cookie : cookies){
		    	    cookie.getName();// get the cookie name
		    	    cookie.getValue(); // get the cookie value
		    	    if ("cookieLanguage".equals(cookie.getName())){
		    	    	//read the value of cookie language
		    	    	cookieLanguage = cookie.getValue(); 
		    	    	break;
		    	    }
		    	}
	    		if("zh".equals(cookieLanguage)){
					temp = new Locale("zh", "CN");   
		            (new CookieLocaleResolver()).setLocale (httpRequest, httpResponse, temp); 
		        } else if("en".equals(cookieLanguage)){
		        	temp = new Locale("en", "US");  
		            (new CookieLocaleResolver()).setLocale (httpRequest, httpResponse, temp);
		        }
	    		
	    	} else {
	    		//read from request
	    		Locale locale = RequestContextUtils.getLocale((HttpServletRequest) request); 
	    		cookieLanguage = locale.getLanguage();
	    		
	    	 
		    	if("zh".equals(cookieLanguage)){
		            temp = new Locale("zh", "CN"); 
		            (new CookieLocaleResolver()).setLocale (httpRequest, httpResponse, temp);
		           
		        } else if("en".equals(cookieLanguage)){
		            temp = new Locale("en", "US"); 
		            (new CookieLocaleResolver()).setLocale (httpRequest, httpResponse, temp); 
		        } 
		    	
	        }
			
			
			
		    
		   
		   // 
		   
	    }
	    //System.out.println("language 1: "+temp);
	    if (temp != null){
	    	request.setAttribute("language", temp.getLanguage());
	    }
	    
	    chain.doFilter(request, response);
	  }

	  @Override
	  public void destroy() {
	  }

	}
