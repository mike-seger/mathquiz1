package com.net128.application.mathquiz.controller.numeric;

public class  Multiplication implements IntOperation {
	public int operate(int op1, int op2) {
		return op1*op2;
	}

	public int invOperate(int op1, int op2) {
		return op1/op2;
	}		
	
	public int maxVariations(double low, double high) {
		int maxOp=(int)Math.floor(Math.sqrt(high));
		int iLow=(int)Math.round(low);
		return ((maxOp+iLow)*maxOp)/2;
	}
	
	public String toString() {
		return Multiplication.class.getName();
	}
}
