package com.net128.application.mathquiz.controller.numeric;
public class IntOperands {
	public int op1;
	public int op2;
	public void swap() {
		int tmp=op1;
		op1=op2;
		op2=tmp;
	}
	@Override
	public String toString() {
		return "IntOperands [op1=" + op1 + ", op2=" + op2 + "]";
	}
}
