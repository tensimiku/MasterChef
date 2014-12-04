package com.tsmk.cooktimer;

import java.util.HashMap;

import android.content.Context;

public class ConvertMaterial {
	private HashMap<String, Integer> converttable = new HashMap<String, Integer>();
	private int[] value = {
			170,
			160,
			160,
			120,
			250,
			160,
			180,
			200,
			180,
			130,
			150,
			140,
			100,
			4,
			8,
			7,
	};
	public ConvertMaterial(Context context) {
		// TODO Auto-generated constructor stub
		
		String[] HashKey = context.getResources().getStringArray(R.array.foodmaterial);
		//use hashmap because it has O(1) and more !
		for(int i=0;i<HashKey.length;i++){
			converttable.put(HashKey[i], value[i]);
		}
		
	}

	public String convert(String key,double value){
		String result = "다시 선택해 주세요.";
		int val = converttable.get(key);
		if(val==0){
			
		} else{
			result = String.format("%.4f 컵",value/converttable.get(key));
		}
		return result;
	}
}
