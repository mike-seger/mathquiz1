package com.net128.application.mathquiz.controller.numeric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomOperandsCache {
	private int  maxSize;
	private List<String> keys=new ArrayList<String>();
	private Map<String, IntOperands> intOperandsMap=new HashMap<String, IntOperands>();
	public RandomOperandsCache(int maxSize) {
		this.maxSize=maxSize;
	}
	public boolean addIntOperands(IntOperands intOperands) {
		if(keys.size()>maxSize) {
			intOperandsMap.remove(keys.remove(0));
		}
		String key=hashKey(intOperands);
		if(intOperandsMap.containsKey(key)) {
			return false;
		}
		intOperandsMap.put(key, intOperands);
		return true;
	}
	private String hashKey(IntOperands intOperands) {
		return intOperands.op1+"."+intOperands.op2;
	}
}

