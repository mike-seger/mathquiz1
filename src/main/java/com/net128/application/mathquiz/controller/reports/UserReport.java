package com.net128.application.mathquiz.controller.reports;

import com.net128.application.mathquiz.persistence.entities.User;

public class UserReport {
	private User user;
	private Object lasLogin;
	private Object numberOfSessions;
	private Object numberOfExercises;
	public UserReport(User user, Object lasLogin, Object numberOfSessions,
			Object numberOfExercises) {
		super();
		this.user = user;
		this.lasLogin = lasLogin;
		this.numberOfSessions = numberOfSessions;
		this.numberOfExercises = numberOfExercises;
	}
	public User getUser() {
		return user;
	}
	public Object getLasLogin() {
		return lasLogin;
	}
	public Object getNumberOfSessions() {
		return numberOfSessions;
	}
	public Object getNumberOfExercises() {
		return numberOfExercises;
	}
	@Override
	public String toString() {
		return "UserReport [user=" + user + ", lasLogin=" + lasLogin
				+ ", numberOfSessions=" + numberOfSessions
				+ ", numberOfExercises=" + numberOfExercises + "]";
	}
}
