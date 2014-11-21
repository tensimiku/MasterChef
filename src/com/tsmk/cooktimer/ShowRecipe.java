package com.tsmk.cooktimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
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
    private SimpleDateFormat ss = new SimpleDateFormat("ss");

    private static TimerAsync async;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private class TimerAsync extends AsyncTask<Calendar, Integer, Void>{
    	
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		isTimerRun = true;
    		actionbar.setDisplayHomeAsUpEnabled(false);
    		mBuilder.setOngoing(true);
    	}
    	
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    		mBuilder.setProgress(0, 0, false).setContentText("취소됨");
    		mBuilder.setOngoing(false);
    		mNotificationManager.notify(NOTYID, mBuilder.build());
    		actionbar.setDisplayHomeAsUpEnabled(true);

    	}
    	
    	@Override
    	protected Void doInBackground(Calendar... params) {
    		// TODO Auto-generated method stub

    		Calendar timerval = params[0];
    		Calendar c = timerval;
    		long calc = SystemClock.elapsedRealtime();
    		long cl = (c.get(Calendar.HOUR_OF_DAY)*3600+c.get(Calendar.MINUTE)*60+c.get(Calendar.SECOND))*1000;
    		long tl = calc + cl;
    		int percentage = (int)cl;
    		while(calc<tl){
    			if(isCancelled()) {
    	    		isTimerRun = false;
    				break;
    			}
    			publishProgress((int)((tl-calc)),percentage);
    			calc = SystemClock.elapsedRealtime();
    			try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		//do something
    		return null;
    	}
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    		int ipg = progress[0]/1000;
    		int pct = progress[1];
    		int hour = ipg/3600;
    		int min = (ipg%3600)/60;
    		int second = ipg%60;    	
    		String s = String.format("%02d:%02d:%02d",hour,min,second);
    		mBuilder.setProgress(pct, pct-progress[0], false);
    		mBuilder.setContentText(s);
    		mNotificationManager.notify(NOTYID, mBuilder.build());
    		timevalue.setText(s);
    		
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		isTimerRun = false;
    		mBuilder.setProgress(0, 0, false).setContentText("끝");
    		mBuilder.setOngoing(false);
    		mNotificationManager.notify(NOTYID, mBuilder.build());
			timerbtn.setText(R.string.start);
    		actionbar.setDisplayHomeAsUpEnabled(true);
    	}
    }
    private final int NOTYID = 3939;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cook_act);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Bundle selrecipe = getIntent().getExtras();
		int sel = selrecipe.getInt("selected");
		int currentpage = selrecipe.getInt("currentpage");
		
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
		mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("요리 타이머")
		        .setContentText("타이머 돌아가는 중")
		        .setAutoCancel(true);
		// Creates an explicit intent for an Activity in your app
		
		
		Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
		resultIntent.setAction(Intent.ACTION_MAIN);
		resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		
		//Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
		
		//resultIntent.addFlags(Intent.FLAG)
		//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		//stackBuilder.addParentStack(ShowRecipe.class);
		// Adds the Intent that starts the Activity to the top of the stack
		//stackBuilder.addNextIntent(resultIntent);
		/* PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );*/
		PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		
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
						try {
							d = ss.parse(input);
						} catch (ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
					}
				}
				if(!isTimerRun&&(d != null)){
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					async = new TimerAsync();
					async.execute(c);
					timerbtn.setText(R.string.stop);
						// mId allows you to update the notification later on.
						mNotificationManager.notify(NOTYID, mBuilder.build());
				}
				else{
					if(isTimerRun){
						async.cancel(true);
					}
					timerbtn.setText(R.string.start);
				}
			}
		});
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerOpened(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

			}
		});
		mDrawerLayout.setScrimColor(Color.parseColor("#00FFFFFF"));
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		rPager.setCurrentItem(currentpage);

        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_timer_res, R.id.drawertext, mPlanetTitles));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	@Override
	public void onBackPressed() {
		if(isTimerRun){
			
		}
		else{
			super.onBackPressed();
		}
		
	};
	
	@Override
	protected void onDestroy() {
		if(isTimerRun){
			
		}
		else{
			super.onDestroy();
		}
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mNotificationManager.cancel(NOTYID);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
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
