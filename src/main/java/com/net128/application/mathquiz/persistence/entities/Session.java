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
public class Session implements Serializable, SingleRefEntity {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Long id;
	private String httpSessionId;
	@ManyToOne //(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private User user;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;	
	private String ipAddress;
	private String userAgent;
	private String acceptLanguage;
	private String referer;
	
	@PrePersist
	public void prePersist() {
		created=new Date();
	}
	
	public Session() {}
	
	public Session(String httpSessionId, User user, String ipAddress, 
		String userAgent, String acceptLanguage, String referer)
	{
		prePersist(); //not called through annotated method?
		this.httpSessionId=httpSessionId;
		this.user=user;
		this.ipAddress=ipAddress;
		this.userAgent=userAgent;
		this.acceptLanguage=acceptLanguage;
		this.referer=referer;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id=id;
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}

	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Object getReference() {
		return getUser();
	}
	
	public void setReference(Object ref) {
		setUser((User)ref);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acceptLanguage == null) ? 0 : acceptLanguage.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((httpSessionId == null) ? 0 : httpSessionId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((referer == null) ? 0 : referer.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result
				+ ((userAgent == null) ? 0 : userAgent.hashCode());
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
		Session other = (Session) obj;
		if (acceptLanguage == null) {
			if (other.acceptLanguage != null)
				return false;
		} else if (!acceptLanguage.equals(other.acceptLanguage))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (httpSessionId == null) {
			if (other.httpSessionId != null)
				return false;
		} else if (!httpSessionId.equals(other.httpSessionId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (referer == null) {
			if (other.referer != null)
				return false;
		} else if (!referer.equals(other.referer))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", httpSessionId=" + httpSessionId
				+ ", user=" + user + ", created=" + created + ", ipAddress="
				+ ipAddress + ", userAgent=" + userAgent + ", acceptLanguage="
				+ acceptLanguage + ", referer=" + referer + "]";
	}
}
