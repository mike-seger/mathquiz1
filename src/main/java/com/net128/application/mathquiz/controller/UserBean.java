package com.net128.application.mathquiz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.net128.application.mathquiz.dao.BaseDao;
import com.net128.application.mathquiz.dao.Dao;
import com.net128.application.mathquiz.persistence.entities.Session;
import com.net128.application.mathquiz.persistence.entities.User;

@ManagedBean
@SessionScoped
public class UserBean {
	private final static Logger logger = LoggerFactory.getLogger(UserBean.class);
	private String name;
	private String password;
	
	//TODO invalidate session, when a new login is done
	private Session session;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;

	}
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Session getSession() {
		return session;
	}

	public void setSessionId(Session session) {
		this.session = session;
	}
	
	public UserBean() {
		super();
	}

	@PostConstruct
	public void postConstruct() {
		init();
	}

	public void init() {
	}
	
	private boolean isEmpty(String s) {
		return s==null || s.length()==0;
	}
	
	private boolean validate() {
		return !(isEmpty(name) || isEmpty(password));
	}
	
	public String login() {
		//TODO invalidate session, when a new login is done
		if(!validate()) {
			return "login";
		}

		Dao<User> userDao=new BaseDao<User>(User.class);
		Map<String, Object> parameters=new HashMap<String,Object>();
		parameters.put("name", getName());
		List<User> users=userDao.findAll("select u from User u where u.name=:name",parameters);
		User user=null;
		if(users.size()==1) {
			user=users.get(0);
		}
		
		if(password!=null && user!=null && !password.equals(user.getPassword())) {
			return "login"; //wrong password
		}
		
		if(user==null) {
			user=userDao.save(new User(name, password));
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession httpSession = (HttpSession) context.getExternalContext().getSession(false);
		HttpServletRequest request = (HttpServletRequest) 
			FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		session=new Session(httpSession.getId(), user, request.getRemoteAddr(), 
			request.getHeader("user-agent"), request.getHeader("accept-language"),
			request.getHeader("referer"));

		logger.info(session.toString());
		BaseDao<Session> sessionDao=new BaseDao<Session>(Session.class);
		session=sessionDao.save(session);
		
		return "welcome";
	}

	@Override
	public String toString() {
		return "UserBean [name=" + name + ", password=" + password + "]";
	}
}
