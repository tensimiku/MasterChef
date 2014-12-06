package com.tsmk.cooktimer;

import android.app.Activity;
import android.os.Bundle;

public class NotiActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(ShowRecipe.r.isPlaying()){
			ShowRecipe.r.stop();
		}
		finish();
	}
}
