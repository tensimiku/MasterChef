package com.tsmk.cooktimer;

import java.util.ArrayList;

import cookmanager.io.RecipeLoader;
import cookmanager.recipe.Recipe;
import cookmanager.recipe.RecipeCategory;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class ShowRecipe extends Activity {
	private ViewPager rPager;
	android.app.ActionBar actionbar;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelative;
    
	
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
		
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(list.get(sel).getRecipeName());
		
		rPager = (ViewPager)findViewById(R.id.recipepager);
		rPager.setAdapter(new CstmPageAdapter());
		mPlanetTitles = new String[]{"여기에 단위변환기가 오면 참 좋겠네요.","네. 단위변환기요."};

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_recipe);
        mDrawerRelative = (RelativeLayout) findViewById(R.id.right_drawer_timer);
        Drawable background = mDrawerRelative.getBackground();
        background.setAlpha(255);
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
	        if(mDrawerLayout.isDrawerOpen(mDrawerRelative)){
				mDrawerLayout.closeDrawer(mDrawerRelative);
	        }
	        else{
	        	mDrawerRelative = (RelativeLayout) findViewById(R.id.right_drawer_timer);
				mDrawerLayout.openDrawer(mDrawerRelative);
	        }
		}
		if(id==R.id.convert){
			//convert on here
	        // Set the adapter for the list view
	        if(mDrawerLayout.isDrawerOpen(mDrawerRelative)){
				mDrawerLayout.closeDrawer(mDrawerRelative);
	        }
	        else{
	        mDrawerRelative = (RelativeLayout) findViewById(R.id.right_drawer_conv);
			mDrawerLayout.openDrawer(mDrawerRelative);
	        }
		}

		return super.onOptionsItemSelected(item);
	}
}
