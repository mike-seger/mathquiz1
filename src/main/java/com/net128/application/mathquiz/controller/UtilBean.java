package com.net128.application.mathquiz.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
public class UtilBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient int row = 0;

    public int getRow() {
        return ++row;
    }

    public class RequestHeader {
    	private String name;
		private String value;
    	public RequestHeader(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
    }
    public List<RequestHeader> getRequestHeaders() {
    	HttpServletRequest request = (HttpServletRequest) 
    		FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	List<RequestHeader>headers=new ArrayList<RequestHeader>();
    	@SuppressWarnings("unchecked")
		Enumeration<String> names=request.getHeaderNames();
    	while(names.hasMoreElements()) {
    		String name=names.nextElement();
    		headers.add(new RequestHeader(name, request.getHeader(name)));
    	}
    	headers.add(new RequestHeader("Remote Addr",request.getRemoteAddr()));
    	headers.add(new RequestHeader("Remote Host",request.getRemoteHost()));
    	return headers;
    }
}