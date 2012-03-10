package com.net128.application.mathquiz.controller.numeric;
public class  Addition implements IntOperation {
	public int operate(int op1, int op2) {
		return op1+op2;
	}

	public int invOperate(int op1, int op2) {
		return op1-op2;
	}		

	public int maxVariations(double low, double high) {
		int maxOp=(int)Math.floor(high/2);
		int iLow=(int)Math.round(low);
		return ((maxOp+iLow)*maxOp)/2;
	}
	
	public String toString() {
		return Addition.class.getName();
	}
}
