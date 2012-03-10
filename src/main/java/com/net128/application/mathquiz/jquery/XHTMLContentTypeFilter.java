package com.net128.application.mathquiz.jquery;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class XHTMLContentTypeFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		try {
			chain.doFilter(request, response);
			String contentType=httpServletResponse.getContentType();
			if(contentType!=null && contentType.contains("application/xhtml+xml;")){
				httpServletResponse.setContentType("text/html;charset=UTF-8");
			}
		} catch (Throwable t) {
			throw new ServletException("",t);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {}

	public void destroy() {}
}