package com.net128.application.mathquiz.controller.numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FactorTableGenerator {
	private void run() {
		generateMap(1000);
	}
	
	private void generateMap(int maxValue) {
		Map<Integer, int []> f1f2Map=new TreeMap<Integer, int []>();
		int count=0;
		for(int f1=1; f1<maxValue; f1++) {
			System.out.println(f1);
			List<Integer> f2List=new ArrayList<Integer>();
			int prevValue=0;
			for(int m=f1; m<=maxValue; m++) {
				int f2=m;
				if(f1*f2>maxValue) {
					break;
				}
				if(!f1f2Map.containsKey(new Integer(f2)) && f2!=prevValue) {
					prevValue=f2;
					f2List.add(f2);
				}
			}
			if(f2List.size()>0) {
				int [] f2Array=new int[f2List.size()];
				int i=0;
				for(Integer f2 : f2List) {	
					f2Array[i++]=f2;
				}
				f1f2Map.put(f1, f2Array);
				count+=f2Array.length;
			}
		}
		System.out.println("f1f2Map:\n");
		for(Integer key : f1f2Map.keySet()) {
			System.out.println(key+": "+Arrays.toString(f1f2Map.get(key)));
		}
		System.out.println("Number of pairs: "+count);
		System.out.println("Number of op1: "+f1f2Map.size());
	}
	
	public static void main(String[] args) {
		new FactorTableGenerator().run();
	}
}
