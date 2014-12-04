package com.tsmk.cooktimer;

import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CstmPageAdapter extends FragmentPagerAdapter {

	
	
	Vector<Fragment> FragmentList;
	
	
	public CstmPageAdapter(FragmentManager fm,Vector<Fragment> af) {
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
