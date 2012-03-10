package com.net128.application.mathquiz.persistence.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints=
    @UniqueConstraint(columnNames={"operator", "mode", "maxResult" }))
public class Level implements Serializable, Comparable<Level> {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Long id;

	@Column(name = "operator")
    @Enumerated(EnumType.ORDINAL)
	private Operator operator;

	@Column(name = "mode")
    @Enumerated(EnumType.ORDINAL)
	private Mode mode;

	@Column(name = "maxResult")
    @Enumerated(EnumType.ORDINAL)
	private MaxResult maxResult;
	
	public Level() {
		this.operator=Operator.ADDITION;
		this.mode=Mode.RESULT;
		this.maxResult=MaxResult.R10;
	}
	
	public Level(Operator operator, Mode mode, MaxResult maxResult) {
		super();
		this.operator = operator;
		this.mode = mode;
		this.maxResult = maxResult;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public MaxResult getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(MaxResult maxResult) {
		this.maxResult = maxResult;
	}
	
	public double getComplexity() {
		return getOperator().getComplexity()*getMaxResult().getComplexity()*getMode().getComplexity();
	}

	public enum Operator {
		ADDITION,
		SUBTRACTION,
		ADDSUB,
		MULTIPLICATION,
		DIVISION,
		MULDIV,
		ADDSUBMULDIV;

		public Operator getRandomSingleOperator() {
			return getRandomSingleOperator(this);
		}
		
		public String getString() {
			return op2StringMap.get(this);
		}
		
		public char getCharacter() {
			return op2charMap.get(this).charValue();
		}
		
		private final static String getString(Operator operator) {
			switch(operator) {
				case ADDSUB: 
					return  Operator.getString(ADDITION)+Operator.getString(SUBTRACTION); 
				case MULDIV:
					return  Operator.getString(MULTIPLICATION)+Operator.getString(DIVISION); 
				case ADDSUBMULDIV:
					return Operator.getString(ADDSUB)+Operator.getString(MULDIV); 
				default: return getChar(operator)+"";
			}
		}
		
		public static Operator findOperator(String operatorString) {
			return string2OpMap.get(operatorString);
		}
		
		public double getComplexity() {
			switch(this) {
				default:
				case ADDITION:
					return 1/2.15;
				case SUBTRACTION:
					return 1/2.1;
				case ADDSUB:
					return 1/2.05;
				case MULTIPLICATION:
					return 1/1.15;
				case DIVISION:
					return 1/1.1;
				case MULDIV:
					return 1/1.05;
				case ADDSUBMULDIV:
					return 1;
				}
		}
		
		public double getMaxComplexity() {
			return 2.15;
		}
		
		private Operator getRandomSingleOperator(Operator operator) {
			double random=Math.random();
			switch(operator) {
				case ADDSUB: 
					if(random<0.5) return ADDITION; 
					else return SUBTRACTION;
				case MULDIV:
					if(random<0.5) return MULTIPLICATION; 
					else return DIVISION;
				case ADDSUBMULDIV:
					if(random<0.5) return getRandomSingleOperator(ADDSUB); 
					else return getRandomSingleOperator(MULDIV);
				default: return this;
			}
		}
		
		private final static char getChar(Operator operator) {
			switch(operator) {
				case ADDITION: return '+';
				case SUBTRACTION: return '-';
				case MULTIPLICATION: return '*';
				case DIVISION: return '/';
				default: return '?';
			}
		}
		
		private final static Map<Operator, Character> op2charMap;
		private final static Map<Operator, String> op2StringMap;
		private final static Map<String, Operator> string2OpMap;
		
		static {
			op2charMap=new HashMap<Operator, Character>();
			for(Operator operator : Operator.values()) {
				op2charMap.put(operator, new Character(getChar(operator)));
			}
			op2StringMap=new HashMap<Operator, String>();
			string2OpMap=new HashMap<String, Operator>();
			for(Operator operator : Operator.values()) {
				op2StringMap.put(operator, getString(operator));
				string2OpMap.put(getString(operator), operator);
			}
		}
	}
	
	public enum Mode {
		RESULT,
		OPERAND1,
		OPERAND2;
		
		public double getComplexity() {
			if(RESULT.equals(this)) {
				//return 0.8; //TODO Implement the operand result modes
				return 1.0;
			}
			return 1;
		}
	}
	
	public enum MaxResult {
		R10,
		R20,
		R30,
		R50,
		R100,
		R200,
		R500,
		R1000;
		
		public int getValue() {
			return Integer.parseInt(this.name().substring(1));
		}
		
		public int getMaxValue() {
			return R1000.getValue();
		}
		
		public MaxResult next() {
			return findOrdinal(ordinal()+1);
		}
		
		public MaxResult prev() {
			return findOrdinal(ordinal()-1);
		}
		
		public MaxResult findOrdinal(int ordinal) {
			for(MaxResult maxResult : values()) {
				if(ordinal == maxResult.ordinal()) {
					return maxResult;
				}
			}
			return this;
		}
		
		public double getComplexity() {
			return Math.log10(getValue())/Math.log10(getMaxValue());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((maxResult == null) ? 0 : maxResult.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Level other = (Level) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maxResult != other.maxResult)
			return false;
		if (mode != other.mode)
			return false;
		if (operator != other.operator)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Level [id=" + id + ", operator=" + operator + ", mode=" + mode
				+ ", maxResult=" + maxResult + "]";
	}

	//@Override
	public int compareTo(Level o) {
		// TODO Auto-generated method stub
		return new Double(this.getComplexity()).compareTo(new Double(o.getComplexity()));
	}
}
