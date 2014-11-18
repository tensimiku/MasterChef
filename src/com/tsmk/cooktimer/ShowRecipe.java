package com.tsmk.cooktimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cookmanager.io.RecipeLoader;
import cookmanager.recipe.Page;
import cookmanager.recipe.Recipe;



public class ShowRecipe extends FragmentActivity {
	private static boolean isTimerRun = false;
	private android.app.ActionBar actionbar;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerTimerRelative;
    private RelativeLayout mDrawerConvRelative;
    private ViewPager rPager;
    private CstmPageAdapter adapter;
    private EditText timeinput;
    private TextView timevalue;
    private Page[] page;
    private Button timerbtn;
    private SeekBar seekrecipe;
    private SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat mmss = new SimpleDateFormat("mm:ss");
    private static TimerAsync async;
    
    private class TimerAsync extends AsyncTask<Calendar, Integer, Void>{

    	@Override
    	protected Void doInBackground(Calendar... params) {
    		// TODO Auto-generated method stub
    		isTimerRun = true;
    		Calendar timerval = params[0];
    		Calendar c = timerval;
    		long calc = SystemClock.elapsedRealtime();
    		long cl = (c.get(Calendar.HOUR_OF_DAY)*3600+c.get(Calendar.MINUTE)*60+c.get(Calendar.SECOND))*1000;
    		long tl = calc + cl;
    		while(calc<tl){
    			if(isCancelled()) {
    	    		isTimerRun = false;
    				break;
    			}
    			publishProgress((int)((tl-calc)/1000));
    			calc = SystemClock.elapsedRealtime();
    			try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		//do something
    		isTimerRun = false;
    		return null;
    	}
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    		int ipg = progress[0];
    		int hour = ipg/3600;
    		int min = (ipg%3600)/60;
    		int second = ipg%60;    	
    		String s = String.format("%02d:%02d:%02d",hour,min,second);
    		timevalue.setText(s);
    		
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
			timerbtn.setText(R.string.start);
    	}
    }
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cook_act);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Bundle selrecipe = getIntent().getExtras();
		int sel = selrecipe.getInt("selected");
		
		RecipeLoader rl = new RecipeLoader(this);
		
		Recipe recipe = rl.getRecipe(sel);
		page = recipe.getPageArray();
		ArrayList<Fragment> af = new ArrayList<Fragment>();
		for(int i=0;i<page.length;i++){
			af.add(new PageFragment(page[i]));
		}
		
		rPager = (ViewPager)findViewById(R.id.recipepager);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_recipe);
		timeinput = (EditText)findViewById(R.id.timeset);
		timerbtn = (Button)findViewById(R.id.setbutton);
		timevalue = (TextView)findViewById(R.id.timevalue);
		seekrecipe = (SeekBar)findViewById(R.id.seekpage);
        mDrawerTimerRelative = (RelativeLayout) findViewById(R.id.right_drawer_timer);
        mDrawerConvRelative = (RelativeLayout) findViewById(R.id.right_drawer_conv);
		actionbar = getActionBar();
		adapter = new CstmPageAdapter(getSupportFragmentManager(),af);
		rPager.setAdapter(adapter);
		rPager.setPageTransformer(true, new DepthPageTransformer());
		
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(recipe.getRecipeName());
		seekrecipe.setMax(page.length-1);

		
		setInputText();

		
		seekrecipe.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				rPager.setCurrentItem(progress);
			}
		});
		
		rPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setInputText();
				seekrecipe.setProgress(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		timerbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date d = null;
				String input = timeinput.getText().toString();
				// TODO Auto-generated method stub
				try {
					d = hhmmss.parse(input);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					try {
						d = mmss.parse(input);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(!isTimerRun&&(d != null)){
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					async = new TimerAsync();
					async.execute(c);
					timerbtn.setText(R.string.stop);
				}
				else{
					async.cancel(true);
					timerbtn.setText(R.string.start);
				}
			}
		});
		mDrawerLayout.setScrimColor(Color.parseColor("#00FFFFFF"));
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_timer_res, R.id.drawertext, mPlanetTitles));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	private void setInputText(){
		int timeval = page[rPager.getCurrentItem()].getTime();
		int timemin = timeval/60;
		int timesec = timeval%60;
		String timestring = timemin+":"+timesec;
		timeinput.setText(timestring);
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
		if(id==R.id.timer){
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
