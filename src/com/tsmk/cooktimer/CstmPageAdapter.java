package com.tsmk.cooktimer;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class CstmPageAdapter extends FragmentPagerAdapter {

	
	
	ArrayList<Fragment> FragmentList;
	
	
	public CstmPageAdapter(FragmentManager fm,ArrayList<Fragment> af) {
		super(fm);
		this.FragmentList = af;
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.FragmentList.size();
	}
	

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.FragmentList.get(arg0);
	}
}
