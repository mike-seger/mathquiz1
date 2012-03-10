package com.net128.application.mathquiz.controller.reports.test;

import java.util.List;

import com.net128.application.mathquiz.controller.ReportBean;
import com.net128.application.mathquiz.controller.reports.ExerciseReport;

public class Test1 {
	public static void main(String[] args) {
		new Test1().test();
	}

	private void test() {
		List<ExerciseReport> result=new ReportBean().getExerciseReport();
		for(ExerciseReport exr : result) {
			System.out.println(exr);
		}
	}
}
