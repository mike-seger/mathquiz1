package com.net128.application.mathquiz.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.net128.application.mathquiz.controller.numeric.IntOperands;
import com.net128.application.mathquiz.controller.numeric.RandomOperands;
import com.net128.application.mathquiz.dao.BaseDao;
import com.net128.application.mathquiz.dao.Dao;
import com.net128.application.mathquiz.dao.SaveHook;
import com.net128.application.mathquiz.persistence.entities.Exercise;
import com.net128.application.mathquiz.persistence.entities.HighScore;
import com.net128.application.mathquiz.persistence.entities.Level;
import com.net128.application.mathquiz.persistence.entities.Level.MaxResult;
import com.net128.application.mathquiz.persistence.entities.Level.Mode;
import com.net128.application.mathquiz.persistence.entities.Level.Operator;
import com.net128.application.mathquiz.persistence.entities.SingleOperationTerm;
import com.net128.application.mathquiz.persistence.entities.User;

@ManagedBean
@SessionScoped
public class LevelBean {
	private final static Logger logger = Logger.getLogger(LevelBean.class);
	private SingleOperationTerm singleOperationTerm;
	private UserBean userBean;
	private Short answer;
	private Level currentLevel;
	private Integer timeSecs=30;
	private HighScore currentHighScore;
	private final static int maxExerciseSize=10;
	private final static int floatingAvgWindowSize=3;
	private final static double correctUpgradeRate=0.9;
	private final static double correctDowngradeRate=0.7;
	
	public String generateLevel() {
		return generateLevel(currentLevel);
	}
	
	private Level getPersitedLevel(Operator operator, Mode mode, MaxResult maxResult) {
		Dao<Level> levelDao=new BaseDao<Level>(Level.class);
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put("operator",operator);
		parameters.put("maxResult",maxResult);
		parameters.put("mode",mode);
		List<Level> levels=levelDao.findFirst("select l from Level l"
			+" where l.operator=:operator and l.mode=:mode and l.maxResult=:maxResult", parameters, 1);
		if(levels!=null && levels.size()==1) {
			return levels.get(0);
		}
		Level level=new Level(operator, mode, maxResult);
		level=levelDao.save(level);
		if(level==null) {
			throw new RuntimeException("Unable to create Level");
		}
		return level;
	}
	
	public Integer getTimeSecs() {
		return timeSecs;
	}
	
	public String selectOperators() {
		answer=null;
		FacesContext context = FacesContext.getCurrentInstance();  
		String operators = context.getExternalContext().getRequestParameterValuesMap().get("operators")[0];
		Operator operator=Level.Operator.findOperator(operators);
		if(currentLevel==null || !currentLevel.getOperator().equals(operators)) {
			currentLevel=getPersitedLevel(operator, Level.Mode.RESULT, Level.MaxResult.R10);
		}
		currentLevel=progressLevel(currentLevel);
		return generateLevel();
	}
	
	private Level progressLevel(final Level level) {
		User user=getUserBean().getSession().getUser();
		Level newLevel=level;
		List<Exercise> exerciseList=getLatestFinishedExercises(user, newLevel.getOperator(), floatingAvgWindowSize);
		if(exerciseList.size()>0)
		/*if(exerciseList.size()>=floatingAvgWindowSize)*/ {
			LevelProgressInfo levelProgressInfo=calcLevelProgressInfo(exerciseList, newLevel);
			newLevel=levelProgressInfo.nextLevel;
			Dao<Level> levelDao=new BaseDao<Level>(Level.class);
			newLevel=levelDao.save(newLevel);
			HighScore highScore=new HighScore(user, newLevel, levelProgressInfo.correctRate, 
				levelProgressInfo.correctUserResponseTimeAverage);
			updateHighScore(highScore);
		} else if(exerciseList.size()>0) {
			newLevel=exerciseList.get(0).getLevel();
		}
		logger.debug("Adjust Level: "+level+" -> "+newLevel+" ("+exerciseList+")");
		return newLevel;
	}
	
	private class LevelProgressInfo {
		private Level nextLevel;
		private double correctRate;
		private double correctUserResponseTimeAverage;
	}
	
	private LevelProgressInfo calcLevelProgressInfo(List<Exercise> exerciseList, Level level) {
		int totalCount=0;
		int correctCount=0;
		boolean allSameLevel=true;
		Level.MaxResult prevMaxResult=null;
		Level.MaxResult maxResult=Level.MaxResult.R10;
		Map<Date, Exercise> sortedExercises=new TreeMap<Date, Exercise>();
		for(Exercise exercise : exerciseList) {
			sortedExercises.put(exercise.getCreated(), exercise);
		}
		Set<Long> exerciseIds=new HashSet<Long>();
		for(Date key : sortedExercises.keySet()) {
			Exercise exercise=sortedExercises.get(key);
			totalCount+=exercise.getSize();
			correctCount+=exercise.getCorrect();
			exerciseIds.add(exercise.getId());
			if(prevMaxResult!=null && !prevMaxResult.equals(exercise.getLevel().getMaxResult())) {
				allSameLevel=false;
				exerciseIds=new HashSet<Long>();
				exerciseIds.add(exercise.getId());
			}
			if(exercise.getLevel().getMaxResult().compareTo(maxResult)>0) {
				maxResult=exercise.getLevel().getMaxResult();
			}
			prevMaxResult=exercise.getLevel().getMaxResult();
		}
		
		LevelProgressInfo levelProgressInfo=new LevelProgressInfo();
		levelProgressInfo.correctRate=(correctCount*1.0)/totalCount;
		levelProgressInfo.correctUserResponseTimeAverage=getCorrectUserResponseTimeAverage(exerciseIds);
		boolean isNewLevel=true;
		
		if(!allSameLevel && !maxResult.equals(level.getMaxResult())) {
			isNewLevel=false;
		} else if(levelProgressInfo.correctRate>=correctUpgradeRate) {
			maxResult=maxResult.next();
		} else if(levelProgressInfo.correctRate<correctDowngradeRate) {
			maxResult=maxResult.prev();
		}
		if(isNewLevel) {
			levelProgressInfo.nextLevel=getPersitedLevel(level.getOperator(), Mode.RESULT, maxResult);
		} else {
			levelProgressInfo.nextLevel=level;
		}
		return levelProgressInfo;
	}
	
	private Double getCorrectUserResponseTimeAverage(Set<Long> exerciseIds) {
		Dao<SingleOperationTerm> singleOperationTermDao=new BaseDao<SingleOperationTerm>(SingleOperationTerm.class);
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put("exerciseIds",exerciseIds);
		Double response=singleOperationTermDao.calculate(
				"select (sum(sot.time)*1.0)/count(sot.id)"
				+" from SingleOperationTerm sot"
				+" where sot.answer=sot.result"
				+"  and sot.exercise.id in :exerciseIds", 
			parameters);
		if(response==null) {
			return 0.0;
		}
		return response;
	}
	
	private List<Exercise> getLatestFinishedExercises(User user, Level.Operator operator, int n) {
		Dao<Exercise> exerciseDao=new BaseDao<Exercise>(Exercise.class);
		Map<String, Object> parameters=new HashMap<String, Object>();
		parameters.put("user",user);
		parameters.put("op",operator);
		return exerciseDao.findFirst("select e from Exercise e"+
			" where e.session.user=:user and e.level.operator=:op"+ 
			" order by e.created desc", parameters, n);
	}
	
	private void updateHighScore(HighScore highScore) {
		Dao<HighScore> highScoreDao=new BaseDao<HighScore>(HighScore.class);
		List<HighScore> list=highScoreDao.findAll("select h from HighScore h where h.user.id="+highScore.getUser().getId());
		HighScore newHighScore=currentHighScore;
		if(list!=null && list.size()==1) {
			HighScore persistedHighScore=list.get(0);
			if(Math.round(1000*(persistedHighScore.calculateScore()-persistedHighScore.getCalculatedScore()))!=0) {
				persistedHighScore=highScoreDao.save(persistedHighScore);
			}
			if(persistedHighScore.calculateScore()<highScore.calculateScore()) {
				persistedHighScore.updateAttributes(highScore);
				newHighScore=highScoreDao.save(persistedHighScore);
			} else {
				newHighScore=persistedHighScore;
			}
		} else {
			newHighScore=highScoreDao.save(highScore);
		}
		currentHighScore=newHighScore;
		logger.debug("Update HighScore: "+highScore+" -> "+newHighScore);
	}
	
	private void updateSingleOperationTerm() {
		if(singleOperationTerm!=null && singleOperationTerm.getId()!=null) {
			///* CHEAT */answer=singleOperationTerm.getResult();
			Dao<SingleOperationTerm> singleOperationTermDao=new BaseDao<SingleOperationTerm>(SingleOperationTerm.class);
			if(singleOperationTerm.getAnswer()==null) {
				singleOperationTerm.setAnswer(answer);
				if(answer!=null && answer.equals(singleOperationTerm.getResult())) {
					singleOperationTerm.getExercise().setCorrect(
						singleOperationTerm.getExercise().getCorrect()+1);
				}
				singleOperationTerm.setTime(new Date().getTime()-singleOperationTerm.getCreated().getTime());
				singleOperationTerm=singleOperationTermDao.save(singleOperationTerm);
			}
			logger.debug("SingleOperationTerm (upd): "+singleOperationTerm);
			answer=null;
		} else {
			logger.info("Not updated: "+singleOperationTerm);
		}
	}
	
	private UserBean getUserBean() {
		if(userBean!=null) {
			return userBean;
		}
		userBean = (UserBean)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get( "userBean");
		return userBean;
	}
	
	private String generateLevel(Level level) {
		if(getUserBean()==null || getUserBean().getSession()==null || getUserBean().getSession().getUser()==null) {
			return "login";
		}

		updateSingleOperationTerm();

		Dao<User> userDao=new BaseDao<User>(User.class);
		List<User> users=userDao.findAll("select user from User user where user.name='"+
			getUserBean().getName()+"'");
		if(users.size()!=1) {
			return "login";
		}
			
		if(singleOperationTerm!=null && singleOperationTerm.getExercise().getExecuted()>=
			singleOperationTerm.getExercise().getSize()) {
			currentLevel=progressLevel(level);
			singleOperationTerm=null;
			return "play-next";
		}

		currentLevel=level;
		answer=null;
		boolean reverse=false;
		int n1,n2,result;
		IntOperands iops;
		Level.Operator operator=level.getOperator().getRandomSingleOperator();
		switch(operator) {
			default:
			case ADDITION:
				iops=RandomOperands.getSummands(level.getMaxResult().getValue());
				n1=iops.op1;
				n2=iops.op2;
				result=n1+n2;
				break;
			case SUBTRACTION:
				iops=RandomOperands.getSummands(level.getMaxResult().getValue());
				n1=iops.op1+iops.op2;
				n2=iops.op2;
				result=n1-n2;
				break;
			case MULTIPLICATION:
				iops=RandomOperands.getFactors(level.getMaxResult().getValue());
				n1=iops.op1;
				n2=iops.op2;
				result=n1*n2;
				break;
			case DIVISION:
				iops=RandomOperands.getFactors(level.getMaxResult().getValue());
				n1=iops.op1*iops.op2;
				n2=iops.op2;
				result=n1/n2;
				break;
		}
		
		if(!operator.equals(level.getOperator())) {
			if(Math.random()<0.5) {
				reverse=true;
			}
		}

		Exercise exercise;
		if(singleOperationTerm!=null) {
			exercise=singleOperationTerm.getExercise();
		} else {
			exercise=newExercise(level);
		}
		exercise.setExecuted(exercise.getExecuted()+1);
		singleOperationTerm=new SingleOperationTerm(exercise, (short)n1, operator, (short)n2, (short)result, reverse);
		singleOperationTerm.setCreated(new Date());
		Dao<SingleOperationTerm> singleOperationTermDao=new BaseDao<SingleOperationTerm>(SingleOperationTerm.class);
		singleOperationTerm=singleOperationTermDao.save(singleOperationTerm);
		logger.debug("SingleOperationTerm (new): "+singleOperationTerm);
		return "play";
	}
	
	private Exercise newExercise(Level level) {
		UserBean userBean = getUserBean();
		Exercise exercise = new Exercise(userBean.getSession(), maxExerciseSize, level);
		BaseDao<Exercise> exerciseDao=new BaseDao<Exercise>(Exercise.class);
		exercise = exerciseDao.save(exercise, new SaveHook());
		logger.info("Exercise (new): "+exercise);
		return exercise;
	}
	
	public int getStepsPrevPercent() {
		if(singleOperationTerm==null) {
			return 0;
		}
		return (int)Math.round((100.0*(singleOperationTerm.getExercise().getExecuted()-1))/
			singleOperationTerm.getExercise().getSize());
	}
	
	public int getStepsPercent() {
		if(singleOperationTerm==null) {
			return 0;
		}
		return (int)Math.round((100.0*singleOperationTerm.getExercise().getExecuted())/
			singleOperationTerm.getExercise().getSize());
	}
	
	public String getStepsInfo() {
		return (singleOperationTerm==null?"0":singleOperationTerm.getExercise().getExecuted())+
			"/"+(singleOperationTerm==null?"0":singleOperationTerm.getExercise().getSize());
	}
	
	public double getScore() {
		double score=0;
		try {
			Dao<Exercise> exerciseDao=new BaseDao<Exercise>(Exercise.class);
			List<Exercise> exercises=exerciseDao.findFirst(
				"select e from Exercise e where e.session.user.id = "+
				userBean.getSession().getUser().getId() +
				" order by e.created desc", null, 1);
			if(exercises.size()>0) {
				Exercise exercise=exercises.get(0);
				if(exercise.getSize()>0) {
					score=(1.0*exercise.getCorrect()/exercise.getSize());
				}
			}
		} catch(Exception e) {
			logger.error("Error occurred in getScore", e);
		}
		return score;
	}
	
	public double getTotalScore() {
		double score=0;
		Dao<SingleOperationTerm> singleOperationTermDao=new BaseDao<SingleOperationTerm>(SingleOperationTerm.class);
		String countAllSingleOperationTerms=
			"select count(sot.id) "+
			"from SingleOperationTerm sot "+
			"where sot.exercise.session.user.id = "+userBean.getSession().getUser().getId();
		Long allCount=singleOperationTermDao.countAll(countAllSingleOperationTerms);
		Long allCorrectCount=singleOperationTermDao.countAll(countAllSingleOperationTerms+
			" and sot.result=sot.answer");
		if(allCount!=0) {
			score=(1.0*allCorrectCount)/allCount;
		}
		return score;
	}
	
	//TODO make this 
	public static Date startOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public int getFinishedExercisesToday() {
		Dao<SingleOperationTerm> singleOperationTermDao=new BaseDao<SingleOperationTerm>(SingleOperationTerm.class);
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		String countQuery=
			"select count(sot.exercise.id) "+
			"from SingleOperationTerm sot "+
			"where sot.exercise.finished >= sot.exercise.size and "+
			" sot.created >= :today group by sot.exercise.id";
		Timestamp today=new Timestamp(startOfDay(new Date()).getTime());
		Map<String, Object>parameters=new HashMap<String,Object>();
		parameters.put("today", today);
		List<SingleOperationTerm> beList=singleOperationTermDao.findAll(countQuery, parameters);
		return beList.size();
	}
	
	public String play() {
		return "play";
	}
	
	public String chooseLevel() {
		return "welcome";
	}

	public SingleOperationTerm getSingleOperationTerm() {
		if(singleOperationTerm==null) {
			generateLevel(currentLevel);
		}
		return singleOperationTerm;
	}
	
	public double getHighScoreValue() {
		if(currentHighScore==null) {
			return 0;
		}
		return currentHighScore.getCalculatedScore();
	}

	public Short getAnswer() {
		return answer;
	}

	public void setAnswer(Short answer) {
		this.answer = answer;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
}
