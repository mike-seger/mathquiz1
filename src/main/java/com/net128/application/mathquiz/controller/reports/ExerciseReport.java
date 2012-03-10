package com.net128.application.mathquiz.controller.reports;

import com.net128.application.mathquiz.persistence.entities.Exercise;

public class ExerciseReport {
	private Exercise exercise;
	private Object started;
	private Object correctCount;
	private Object avgTime;
	public ExerciseReport(Exercise exercise, Object started, Object correctCount, Object avgTime) {
		this.exercise=exercise;
		this.started=started;
		this.correctCount=correctCount;
		this.avgTime=avgTime;
	}
	
	public Object getStarted() {
		return started;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public Object getCorrectCount() {
		return correctCount;
	}

	public Object getAvgTime() {
		return avgTime;
	}

	@Override
	public String toString() {
		return "ExerciseReport [exercise=" + exercise + ", started=" + started
				+ ", correctCount=" + correctCount + ", avgTime=" + avgTime
				+ "]";
	}
}