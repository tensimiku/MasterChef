package com.tsmk.cooktimer;

import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	Vector<Fragment> vectorlist;
	public TabsPagerAdapter(FragmentManager fm,Vector<Fragment> v) {
		super(fm);
		vectorlist = v;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
        switch (arg0) {
        case 0:
            return vectorlist.get(0);
        case 1:
            return vectorlist.get(1);
        case 2:
            return vectorlist.get(2);
        }
 
        return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vectorlist.size();
	}

}
