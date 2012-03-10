package com.net128.application.mathquiz.persistence;

import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.net128.application.mathquiz.dao.BaseDao;

@ManagedBean
@ApplicationScoped
public class PersistenceManagerBean {
	private final static Logger logger = Logger.getLogger(BaseDao.class);
	private static final PersistenceManagerBean singleton = new PersistenceManagerBean();
	protected EntityManagerFactory emf;

	public static synchronized PersistenceManagerBean getInstance() {
		return singleton;
	}

	private PersistenceManagerBean() {
	}

	public EntityManagerFactory getEntityManagerFactory() {
		if (emf == null)
			createEntityManagerFactory();
		return emf;
	}

	@PreDestroy
	public void closeEntityManagerFactory() {
		if (emf != null) {
			emf.close();
			emf = null;
			logger.debug("n*** Persistence finished at " + new java.util.Date());
		}
	}

	private void createEntityManagerFactory() {
//		this.emf = Persistence.createEntityManagerFactory("mathquiz");
		ServletContext servletContext = (ServletContext) 
			FacesContext.getCurrentInstance().getExternalContext().getContext();
		String persistenceUnit=servletContext.getInitParameter("persistence.unit.mathquiz");
		if(persistenceUnit==null) {
			persistenceUnit="mathquiz";
		}	
		this.emf = Persistence.createEntityManagerFactory(persistenceUnit);
		//this.emf = Persistence.createEntityManagerFactory("database/objectdb/mathquiz.odb");
		logger.debug("n*** Persistence started at " + new java.util.Date());
	}
}