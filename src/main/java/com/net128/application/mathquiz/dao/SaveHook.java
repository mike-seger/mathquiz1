package com.net128.application.mathquiz.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.net128.application.mathquiz.persistence.entities.Exercise;
import com.net128.application.mathquiz.persistence.entities.Level;

public class SaveHook {
	public void process(EntityManager entityManager, Object o) {
		if(o instanceof Exercise) {
			Exercise exercise=(Exercise)o;
			Level level=exercise.getLevel();
			if(level!=null) {
				if(level.getId()!=null) {
					level=entityManager.find(Level.class, level.getId());
				} else {
					TypedQuery<Level> query=entityManager.createQuery( 
							"select level from Level level "+
							"where "+
							" level.operator=:operator"+
							" and level.mode=:mode"+
							" and level.maxResult=:maxResult", Level.class);
					query.setParameter("operator", level.getOperator());
					query.setParameter("mode", level.getMode());
					query.setParameter("maxResult", level.getMaxResult());
					List<Level> levels=query.getResultList();
					if(levels.size()==1) {
						level=levels.get(0);
					}
					if(level==null) {
						level=entityManager.merge(level);
					}
				}
				
				exercise.setLevel(level);
			}
		}
	}
}
