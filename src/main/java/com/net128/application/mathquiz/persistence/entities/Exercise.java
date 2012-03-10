package com.net128.application.mathquiz.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.net128.application.mathquiz.dao.SingleRefEntity;

@Entity
public class Exercise implements Serializable, SingleRefEntity {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne //(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Session session;
	private Level level;
	private int size;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;	
	private int executed;
	private int correct;
	
	public Exercise() {}
	
	public Exercise(Session session, int size, Level level) {
		this.session=session;
		this.size=size;
		this.setLevel(level);
	}
	
	@PrePersist
	public void prePersist() {
		if(created==null) {
			created=new Date();
		}
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id=id;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	public Object getReference() {
		return getSession();
	}
	
	public void setReference(Object ref) {
		setSession((Session)ref);
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}

	public void setExecuted(int executed) {
		this.executed = executed;
	}

	public int getExecuted() {
		return executed;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + correct;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + executed;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((session == null) ? 0 : session.hashCode());
		result = prime * result + size;
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
		Exercise other = (Exercise) obj;
		if (correct != other.correct)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (executed != other.executed)
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
		if (session == null) {
			if (other.session != null)
				return false;
		} else if (!session.equals(other.session))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Exercise [id=" + id + ", session=" + session + ", level="
				+ level + ", size=" + size + ", created=" + created
				+ ", executed=" + executed + ", correct=" + correct + "]";
	}
}
