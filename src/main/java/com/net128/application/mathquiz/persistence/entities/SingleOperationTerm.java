package com.net128.application.mathquiz.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.net128.application.mathquiz.dao.SingleRefEntity;

@Entity
public class SingleOperationTerm implements Serializable, SingleRefEntity {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne //(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Exercise exercise;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	private Long time;
	private Short number1;
	private Level.Operator operator;
	private Short number2;
	private Short result;
	private Short answer;
	private boolean reverse;

	@PrePersist
	public void prePersist() {
		created=new Date();
		if(answer!=null && answer.equals(result)) {
			updateTime();
		}
	}
	
	private void updateTime() {
		setTime(new Date().getTime()-created.getTime());
	}

	@PreUpdate
	public void preUpdate() {
		updateTime();
	}

	public SingleOperationTerm() {	
	}
	
	public SingleOperationTerm(Exercise exercise, Short number1, Level.Operator operator, Short number2, 
			Short result, boolean reverse) {
		super();
		this.exercise=exercise;
		this.number1 = number1;
		this.operator = operator;
		this.number2 = number2;
		this.result = result;
		this.reverse = reverse;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public Exercise getExercise() {
		return exercise;
	}
	
	public Object getReference() {
		return getExercise();
	}
	
	public void setReference(Object ref) {
		setExercise((Exercise)ref);
	}

	public Short getNumber1() {
		return number1;
	}

	public void setNumber1(Short number1) {
		this.number1 = number1;
	}

	public Level.Operator getOperator() {
		return operator;
	}

	public void setOperator(Level.Operator operator) {
		this.operator = operator;
	}

	public Short getNumber2() {
		return number2;
	}

	public void setNumber2(Short number2) {
		this.number2 = number2;
	}

	public Short getResult() {
		return result;
	}

	public void setResult(Short result) {
		this.result = result;
	}

	public Short getAnswer() {
		return answer;
	}

	public void setAnswer(Short answer) {
		this.answer = answer;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id=id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created=created;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time=time;
	}

	@Override
	public String toString() {
		return "SingleOperationTerm [id=" + id + ", exercise=" + exercise
				+ ", created=" + created + ", time=" + time + ", number1="
				+ number1 + ", operator=" + operator + ", number2=" + number2
				+ ", result=" + result + ", answer=" + answer + ", reverse="
				+ reverse + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((exercise == null) ? 0 : exercise.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((number1 == null) ? 0 : number1.hashCode());
		result = prime * result + ((number2 == null) ? 0 : number2.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + (reverse ? 1231 : 1237);
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		SingleOperationTerm other = (SingleOperationTerm) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (exercise == null) {
			if (other.exercise != null)
				return false;
		} else if (!exercise.equals(other.exercise))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (number1 == null) {
			if (other.number1 != null)
				return false;
		} else if (!number1.equals(other.number1))
			return false;
		if (number2 == null) {
			if (other.number2 != null)
				return false;
		} else if (!number2.equals(other.number2))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (reverse != other.reverse)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}
}
