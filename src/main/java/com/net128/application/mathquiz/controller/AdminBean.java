package com.net128.application.mathquiz.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import com.net128.application.mathquiz.dao.BaseDao;
import com.net128.application.mathquiz.persistence.entities.SingleOperationTerm;
import com.net128.application.mathquiz.persistence.entities.Exercise;
import com.net128.application.mathquiz.persistence.entities.Session;
import com.net128.application.mathquiz.persistence.entities.User;

@ManagedBean
public class AdminBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String listHeaders() {
		return "list-headers";
	}
	
	public String listTests() {
		return "list-tests";
	}
	
	public String listExercises() {
		return "list-exercises";
	}
	
	public String listScores() {
		return "list-scorees";
	}
	
	public String resetData() {
		new BaseDao<SingleOperationTerm>(SingleOperationTerm.class).deleteAll();
		new BaseDao<Exercise>(Exercise.class).deleteAll();
		new BaseDao<Session>(Session.class).deleteAll();
		new BaseDao<User>(User.class).deleteAll();

		System.out.println("Exercise: "+new BaseDao<Exercise>(Exercise.class).countAll("select count(x.id) from Exercise x"));
		System.out.println("Session: "+new BaseDao<Session>(Session.class).countAll("select count(x.id) from Session x"));
		System.out.println("User: "+new BaseDao<User>(User.class).countAll("select count(x.id) from User x"));
		System.out.println("SingleOperationTerm: "+new BaseDao<SingleOperationTerm>(SingleOperationTerm.class).countAll("select count(x.id) from SingleOperationTerm x"));

		return "login";
	}
}