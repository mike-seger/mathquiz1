package com.net128.application.mathquiz.controller.numeric;

import java.util.HashMap;
import java.util.Map;

public class RoCache {
	private Map<String, RandomOperandsCache> roCacheMap=
		new HashMap<String, RandomOperandsCache>();
	public boolean addRandomOperands(IntOperands intOperands, IntOperation intOpearation, 
			double lowestRandomVal, int maxOperand, int maxResult, int maxCachedValues) {
		String rocKey=rocKey(intOpearation, lowestRandomVal, maxOperand, maxResult);
		RandomOperandsCache randomOperandsCache=roCacheMap.get(rocKey);
		if(randomOperandsCache==null) {
			randomOperandsCache=new RandomOperandsCache(maxCachedValues);
			roCacheMap.put(rocKey, randomOperandsCache);
		}
		return randomOperandsCache.addIntOperands(intOperands);
	}
	private String rocKey(IntOperation intOpearation, double lowestRandomVal, int maxOperand, int maxResult) {
		return intOpearation+"-"+lowestRandomVal+"-"+maxOperand+"-"+maxResult;
	}
}

