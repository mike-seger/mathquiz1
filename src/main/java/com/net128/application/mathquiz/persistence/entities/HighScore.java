package com.net128.application.mathquiz.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class HighScore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;	

	@OneToOne 
	private User user;

	@ManyToOne 
	private Level level;
	
	private double correctRate;
	private double avgCorrectResponseTime;
	
	private static final int minimalTimeOverhead=5000;
	
	private double calculatedScore;

	public HighScore() {}

	public HighScore(User user, Level level, double correctRate, double avgCorrectResponseTime) {
		super();
		this.user = user;
		this.level = level;
		this.correctRate = correctRate;
		this.avgCorrectResponseTime = avgCorrectResponseTime;
	}

	public void updateAttributes(HighScore hisghScore) {
		this.user = hisghScore.user;
		this.level = hisghScore.level;
		this.correctRate = hisghScore.correctRate;
		this.avgCorrectResponseTime = hisghScore.avgCorrectResponseTime;
		prePersist();
	}
	
	@PrePersist
	public void prePersist() {
		if(modified==null) {
			modified=new Date();
		}
		calculatedScore=calculateScore();
	}
	
	@PreUpdate
	public void preUpdate() {
		prePersist();
	}
	
	public double calculateScore() {
		double calculatedScore= getRespnseTimeRating(getAvgCorrectResponseTime())*(level!=null?level.getComplexity():0)*getCorrectRate();
		return calculatedScore;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id=id;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public double getCorrectRate() {
		return correctRate;
	}

	public void setCorrectRate(double correctRate) {
		this.correctRate = correctRate;
	}

	public double getAvgCorrectResponseTime() {
		return avgCorrectResponseTime;
	}

	public void setAvgCorrectRespnseTime(double avgCorrectResponseTime) {
		this.avgCorrectResponseTime = avgCorrectResponseTime;
	}

	public double getCalculatedScore() {
		return calculatedScore;
	}

	public void setCalculatedScore(double calculatedScore) {
		this.calculatedScore = calculatedScore;
	}
	
	private double getRespnseTimeRating(double responseTime) {
		if(responseTime<=0) {
			return 0.5;
		}
		
		responseTime-=minimalTimeOverhead;
		if(responseTime<=4000) {
			return 1;
		} else if(responseTime<=5000) {
			return 1/1.01;
		} else if(responseTime<=6000) {
			return 1/1.05;
		} else if(responseTime<=7000) {
			return 1/1.1;
		} else if(responseTime<=8000) {
			return 1/1.17;
		} else if(responseTime<=9000) {
			return 1/1.25;
		} else if(responseTime<=10000) {
			return 1/1.35;
		} else if(responseTime<=11000) {
			return 1/1.46;
		}
		return 1/1.5;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(avgCorrectResponseTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(calculatedScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(correctRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result
				+ ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HighScore other = (HighScore) obj;
		if (Double.doubleToLongBits(avgCorrectResponseTime) != Double
				.doubleToLongBits(other.avgCorrectResponseTime))
			return false;
		if (Double.doubleToLongBits(calculatedScore) != Double
				.doubleToLongBits(other.calculatedScore))
			return false;
		if (Double.doubleToLongBits(correctRate) != Double
				.doubleToLongBits(other.correctRate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HighScore [id=" + id + ", modified=" + modified + ", user="
				+ user + ", level=" + level + ", correctRate=" + correctRate
				+ ", avgCorrectResponseTime=" + avgCorrectResponseTime
				+ ", calculatedScore=" + calculatedScore + "]";
	}
}
