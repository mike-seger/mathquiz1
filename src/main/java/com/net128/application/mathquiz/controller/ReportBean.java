package com.net128.application.mathquiz.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.net128.application.mathquiz.controller.reports.ExerciseReport;
import com.net128.application.mathquiz.controller.reports.UserReport;
import com.net128.application.mathquiz.dao.BaseDao;
import com.net128.application.mathquiz.dao.Dao;
import com.net128.application.mathquiz.persistence.entities.HighScore;
import com.net128.application.mathquiz.persistence.entities.Session;
import com.net128.application.mathquiz.persistence.entities.SingleOperationTerm;
import com.net128.application.mathquiz.persistence.entities.User;

@ManagedBean
@SessionScoped
public class ReportBean {
	private Long exerciseId;
	private Long sessionId;
	private Long userId;
	private User user;
	
	public List<SingleOperationTerm> getSingleOperationTermList() {
		Dao<SingleOperationTerm> singleOperationTermDao=new BaseDao<SingleOperationTerm>(SingleOperationTerm.class);
		return singleOperationTermDao.findAll(
			"select sot from SingleOperationTerm sot"
			+(exerciseId!=null?
			" where sot.exercise.id="+exerciseId:"")
			+" order by sot.created");
	}
	
	public String showSingleOperationTermList(Long exerciseId) {
		this.exerciseId = exerciseId;
		return "list-tests";
	}
	
	public List<ExerciseReport> getExerciseReport() {
		List<ExerciseReport> result=new BaseDao<ExerciseReport>(ExerciseReport.class).findAll(
			"select NEW "+ExerciseReport.class.getName()+"(ex"
				+", min(sot.created)"
				+", count(sot.id)"
				+", avg(sot.time)"
				+")"
			+" from Exercise ex, SingleOperationTerm sot"
			+" where ex.id = sot.exercise.id"
			+"  and sot.result = sot.answer"
			+(sessionId!=null?" and ex.session.id = "+sessionId:"")
			+" group by ex.id "
			+" order by ex.created desc"
			);
		return result;
	}
	
	public String showExercises(Long sessionsId) {
		this.sessionId = sessionsId;
		return "list-exercises";
	}
	
	public List<Session> getSessions() {
		Dao<Session> sessionDao=new BaseDao<Session>(Session.class);
		return sessionDao.findAll(
			"select s from Session s"
			+(userId!=null?
			" where s.user.id="+userId:"")
			+" order by s.created desc");
	}
	
	public String showSessions(Long userId) {
		this.userId = userId;
		Dao<User> userDao=new BaseDao<User>(User.class);
		user=userDao.findById(userId);
		return "list-sessions";
	}
	
	public List<UserReport> getUserReport() {
		List<UserReport> result=new BaseDao<UserReport>(UserReport.class).findAll(
			"select NEW "+UserReport.class.getName()+"(u"
				+", max(s.created)"
				+", count(s.id)"
				+", count(s.id)"
				+")"
			+" from User u, Session s"
			+" where u.id = s.user.id "
			+" group by u.id"
			+" order by u.name"
			);
		return result;
	}
	
	public List<HighScore> getHighScoreReport() {
		Dao<HighScore> highScoreDao=new BaseDao<HighScore>(HighScore.class);
		List<HighScore> highScores=highScoreDao.findFirst("select hs from HighScore hs order by hs.calculatedScore desc", null, 20);
		for(HighScore highScore : highScores) {
			if(Math.round((highScore.calculateScore()-highScore.getCalculatedScore())*1000)!=0) {
				highScore.setCalculatedScore(highScore.calculateScore());
				highScoreDao.save(highScore);
			}
		}
		return highScores;
	}

	public Long getExerciseId() {
		return exerciseId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public User getUser() {
		return user;
	}
}
