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

import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;


public class AuthenticationFilter implements Filter {
	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		String requestURI = ((HttpServletRequest) req).getRequestURI();
		if (StringUtils.contains(requestURI, "donor_login.xhtml")) {
			((HttpServletResponse) resp).sendRedirect("../donor_login.xhtml");
			return;
		}
		if (((HttpServletRequest) req).getSession().getAttribute(SessionManagementBean.AUTH_KEY) == null) {
			((HttpServletResponse) resp).sendRedirect("../index.xhtml");
		}
		else {
			chain.doFilter(req, resp);
		}
	}
	
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
	
	public void destroy() {
		config = null;
	}
	
	
	private FilterConfig config;
}