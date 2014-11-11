package com.tsmk.cooktimer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
        switch (arg0) {
        case 0:
            // Top Rated fragment activity
            return new RecipeList_tab1();
        case 1:
            // Games fragment activity
            return new RecipeList_tab2();
        case 2:
            // Movies fragment activity
            return new RecipeList_tab3();
        }
 
        return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
