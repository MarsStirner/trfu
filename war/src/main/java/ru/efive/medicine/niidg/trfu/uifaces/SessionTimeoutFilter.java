package ru.efive.medicine.niidg.trfu.uifaces;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class SessionTimeoutFilter implements Filter {
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			if (isSessionControlRequiredForThisResource(httpServletRequest)) {
				if (isSessionInvalid(httpServletRequest)) {
					String timeoutUrl = httpServletRequest.getContextPath() + "/" + getTimeoutPage();
					System.out.println("Session is invalid! redirecting to timeoutpage : "+ timeoutUrl);
					httpServletResponse.sendRedirect(timeoutUrl);
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}
	
	private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {
		String requestPath = httpServletRequest.getRequestURI();
		return !StringUtils.contains(requestPath,getTimeoutPage()) && !StringUtils.contains(requestPath, getDonorTimeoutPage());
	}
	
	private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {
		return (httpServletRequest.getRequestedSessionId() != null) && !httpServletRequest.isRequestedSessionIdValid();
	}
	
	public void destroy() {
		
	}
	
	public String getTimeoutPage() {
		return timeoutPage;
	}
	
	public String getDonorTimeoutPage() {
		return donorTimeoutPage;
	}
	
	
	private String timeoutPage = "index.xhtml";
	private String donorTimeoutPage = "donor_login.xhtml";
}