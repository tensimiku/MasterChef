package com.tsmk.cooktimer;

import java.util.ArrayList;

import cookmanager.io.RecipeLoader;
import cookmanager.recipe.Page;
import cookmanager.recipe.Recipe;
import cookmanager.recipe.RecipeCategory;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class ShowRecipe extends FragmentActivity {
	android.app.ActionBar actionbar;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerTimerRelative;
    private RelativeLayout mDrawerConvRelative;

    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cook_act);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Bundle selrecipe = getIntent().getExtras();
		int sel = selrecipe.getInt("selected");
		int selrc = selrecipe.getInt("category");
		
		RecipeLoader rl = new RecipeLoader(this);
		
		ArrayList<Recipe> list = rl.getRecipeArrayList(RecipeCategory.getCategory(selrc), false);
		Recipe recipe = list.get(sel);
		Page[] page = recipe.getPageArray();
		ArrayList<Fragment> af = new ArrayList<Fragment>();
		for(int i=0;i<page.length;i++){
			af.add(new PageFragment(page[i]));
		}
		
		ViewPager rPager = (ViewPager)findViewById(R.id.recipepager);
		rPager.setAdapter(new CstmPageAdapter(getSupportFragmentManager(),af));
		rPager.setPageTransformer(true, new DepthPageTransformer());
		
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(recipe.getRecipeName());
		



		mPlanetTitles = new String[]{"여기에 단위변환기가 오면 참 좋겠네요.","네. 단위변환기요."};

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_recipe);
        mDrawerTimerRelative = (RelativeLayout) findViewById(R.id.right_drawer_timer);
        mDrawerConvRelative = (RelativeLayout) findViewById(R.id.right_drawer_conv);
        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_timer_res, R.id.drawertext, mPlanetTitles));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if(id==R.id.timerset){
			//timer on here
	        // Set the adapter for the list view
	        if(mDrawerLayout.isDrawerOpen(mDrawerTimerRelative)){
				mDrawerLayout.closeDrawer(mDrawerTimerRelative);
	        }
	        else{
				mDrawerLayout.openDrawer(mDrawerTimerRelative);
	        }
	        if(mDrawerLayout.isDrawerOpen(mDrawerConvRelative)){
				mDrawerLayout.closeDrawer(mDrawerConvRelative);
	        }

		}
		if(id==R.id.convert){
			//convert on here
	        // Set the adapter for the list view
	        if(mDrawerLayout.isDrawerOpen(mDrawerConvRelative)){
				mDrawerLayout.closeDrawer(mDrawerConvRelative);
	        }
	        else{
				mDrawerLayout.openDrawer(mDrawerConvRelative);
	        }
	        if(mDrawerLayout.isDrawerOpen(mDrawerTimerRelative)){
				mDrawerLayout.closeDrawer(mDrawerTimerRelative);
	        }
		}

		return super.onOptionsItemSelected(item);
	}
}
