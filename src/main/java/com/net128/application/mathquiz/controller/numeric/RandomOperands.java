package com.net128.application.mathquiz.controller.numeric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomOperands {
	private final static Logger logger = LoggerFactory.getLogger(RandomOperands.class);
	private final static int maxRetries=50;
	private static RoCache roCache=new RoCache();
	private static Multiplication theMultiplication=new Multiplication();	
	private static Addition theAddition=new Addition();
	

	public static IntOperands getFactors(int maxProduct) {
		return getRandomOperands(theMultiplication, 1.3, (int)Math.floor(Math.sqrt(maxProduct)), maxProduct);
	}

	public static IntOperands getSummands(int maxSum) {
		return getRandomOperands(theAddition, 0.47, maxSum/2, maxSum);
	}
			
	private static int getBoundedRandom(double low, double high) {
		if(high<low) {
			double tmp=low;
			low=high;
			high=tmp;
		}
		return (int)Math.round((high-low)*Math.random()+low);
	}

	private static IntOperands getRandomOperands(IntOperation intOpearation, 
			double lowestRandomVal, int maxOperand, int maxResult) {
		IntOperands intOperands=getRandomOperandsWork(
			intOpearation, lowestRandomVal, maxOperand, maxResult);
		int maxCachedValues=intOpearation.maxVariations(0, maxResult);
		int retry=0;
		while(!roCache.addRandomOperands(intOperands, intOpearation, 
				lowestRandomVal, maxOperand, maxResult, maxCachedValues)) {
			intOperands=getRandomOperandsWork(
				intOpearation, lowestRandomVal, maxOperand, maxResult);
			if(retry++ > maxRetries) {
				break;
			}
		}
		return intOperands;
	}
	
	private static IntOperands getRandomOperandsWork(IntOperation intOpearation, 
			double lowestRandomVal, int maxOperand, int maxResult) {
		IntOperands intOperands=new IntOperands();
		intOperands.op1=getBoundedRandom(lowestRandomVal, maxOperand);
		logger.debug("getRandomOperands 1: "+lowestRandomVal+"/"+maxResult+" -> "+intOperands.op1);
		maxOperand=intOpearation.invOperate(maxResult,intOperands.op1);
		intOperands.op2=getBoundedRandom(lowestRandomVal, maxOperand);
		logger.debug("getRandomOperands 2: "+lowestRandomVal+"/"+maxOperand+" -> "+intOperands.op2);
		if(Math.random()<=0.5) {
			intOperands.swap();
		}
		return intOperands;
	}
		
	private void test() {
		logger.info(Addition.class.getSimpleName());
		for(int i=0; i<100; i++) {
			IntOperands intOperands=getSummands(10);
			System.out.println(intOperands+"="+(intOperands.op1+intOperands.op2));			
		}
		for(int i=0; i<100; i++) {
			IntOperands intOperands=getSummands(50);
			System.out.println(intOperands+"="+(intOperands.op1+intOperands.op2));			
		}
		logger.info(Multiplication.class.getSimpleName());
		for(int i=0; i<100; i++) {
			IntOperands intOperands=getFactors(50);
			System.out.println(intOperands+"="+(intOperands.op1+intOperands.op2));			
		}
	}
	
	public static void main(String [] args) {
		new RandomOperands().test();
	}
}
