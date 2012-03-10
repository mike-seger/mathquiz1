package com.net128.application.mathquiz.dao;

import java.util.List;
import java.util.Map;

public interface Dao<T> {
	T save(T dao);
	int deleteAll();
	boolean delete(Long id);
	int update(String query);
	int update(String query, Map<String, Object> parameters);
	T findById(Long id);
	List<T> findAll(String query);
	List<T> findAll(String query, Map<String, Object> parameters);
	List<T> findFirst(String queryString, Map<String, Object> parameters, int numberOfRecords);
	Long countAll(String query);
	Long countAll(String query, Map<String, Object> parameters);
	Double calculate(String query);
	Double calculate(String query, Map<String, Object> parameters);
}
