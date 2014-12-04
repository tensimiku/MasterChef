package com.tsmk.cooktimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
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
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import cookmanager.io.RecipeLoader;
import cookmanager.recipe.Page;
import cookmanager.recipe.Recipe;



public class ShowRecipe extends FragmentActivity {
	private static boolean isTimerRun = false;
	private static boolean isTimerShow = false;
	private static boolean isConvShow = false;
	
	
	private boolean isOpening = false;
	
	
	private android.app.ActionBar actionbar;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelative;
    private ViewPager rPager;
    private CstmPageAdapter adapter;
    
    private Page[] page;
    private SeekBar seekrecipe;
    private SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat mmss = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat ss = new SimpleDateFormat("ss");

    private EditText timeinput;
    private TextView timevalue;
    private TextView drawertimertext;
    private Button timerbtn;
    
    private TextView drawertext;
    private Spinner convlist;
    private EditText origvalue;
    private TextView convvalue;
    private Button convertbtn;
    
    private ConvertMaterial convmate;
    
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
    private boolean isSeekbarControlled = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cook_act);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final int smoothscrollbarval = 100;
		Bundle selrecipe = getIntent().getExtras();
		int sel = selrecipe.getInt("selected");
		int currentpage = selrecipe.getInt("currentpage");
		convmate = new ConvertMaterial(getApplicationContext());
		RecipeLoader rl = new RecipeLoader(this);
		
		Recipe recipe = rl.getRecipe(sel);
		page = recipe.getPageArray();
		Vector<Fragment> af = new Vector<Fragment>();
		for(int i=0;i<page.length;i++){
			PageFragment pf = new PageFragment();
			Bundle bd = new Bundle();
			bd.putParcelable("page", page[i]);
			pf.setArguments(bd);
			af.add(pf);
		}
		
		rPager = (ViewPager)findViewById(R.id.recipepager);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_recipe);
		
		initDrawerViewRes();
		
		seekrecipe = (SeekBar)findViewById(R.id.seekpage);
        mDrawerRelative = (RelativeLayout) findViewById(R.id.right_drawer);
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
		seekrecipe.setMax((page.length-1)*smoothscrollbarval);

		
		setInputText();

		
		seekrecipe.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int maxOffset;
			int currentOffset;
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				isSeekbarControlled = false;
				rPager.endFakeDrag();
				seekrecipe.setProgress(rPager.getCurrentItem()*smoothscrollbarval);
				currentOffset=0;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				isSeekbarControlled = true;
				maxOffset = rPager.getWidth();
				rPager.beginFakeDrag();
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser){
					int offset = (int)((maxOffset/smoothscrollbarval)*(progress%smoothscrollbarval));
					int dragby = -1 * (offset - currentOffset);
					rPager.fakeDragBy(dragby);
					currentOffset=offset;
					if(dragby<0){
						rPager.setCurrentItem(progress/smoothscrollbarval,false);
					} else{
						rPager.setCurrentItem((progress+smoothscrollbarval-1)/smoothscrollbarval,false);
					}
				}
			}
		});
		
		rPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setInputText();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if(!isSeekbarControlled){
					seekrecipe.setProgress((int)((arg0+arg1)*smoothscrollbarval));
				}
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
		
		convertbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					convvalue.setText(
								convmate.convert(String.valueOf(convlist.getSelectedItem()) , 
								Double.parseDouble(origvalue.getText().toString())
							)
						);
				} catch (NumberFormatException e){
					convvalue.setText("변환 할수 없는 값입니다.");
				}
			}
		});
		
		if(isConvShow){
			showConv();
			hideTimer();
		}
		if(isTimerShow){
			showTimer();
			hideConv();
		}

		mDrawerLayout.setScrimColor(Color.parseColor("#00FFFFFF"));
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
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		rPager.setCurrentItem(currentpage);

        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_timer_res, R.id.drawertext, mPlanetTitles));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	void initDrawerViewRes(){
		timeinput = (EditText)findViewById(R.id.timeset);
		timerbtn = (Button)findViewById(R.id.setbutton);
		timevalue = (TextView)findViewById(R.id.timevalue);
		drawertimertext = (TextView)findViewById(R.id.drawertimertext);
		
		drawertext = (TextView)findViewById(R.id.drawertext);
	    convlist = (Spinner)findViewById(R.id.convlist);
	    origvalue = (EditText)findViewById(R.id.origvalue);
	    convvalue = (TextView)findViewById(R.id.convvalue);
	    convertbtn = (Button)findViewById(R.id.convertbtn);
	}
	void hideTimer(){
		isTimerShow = false;
		timeinput.setVisibility(View.INVISIBLE);
		timerbtn.setVisibility(View.INVISIBLE);
		timevalue.setVisibility(View.INVISIBLE);
		drawertimertext.setVisibility(View.INVISIBLE);
	}
	void hideConv(){
		isConvShow = false;
		drawertext.setVisibility(View.INVISIBLE);
		convlist.setVisibility(View.INVISIBLE);
		origvalue.setVisibility(View.INVISIBLE);
		convvalue.setVisibility(View.INVISIBLE);
		convertbtn.setVisibility(View.INVISIBLE);
	}
	void showTimer(){
		isTimerShow = true;
		timeinput.setVisibility(View.VISIBLE);
		timerbtn.setVisibility(View.VISIBLE);
		timevalue.setVisibility(View.VISIBLE);
		drawertimertext.setVisibility(View.VISIBLE);
	}
	void showConv(){
		isConvShow = true;
		drawertext.setVisibility(View.VISIBLE);
		convlist.setVisibility(View.VISIBLE);
		origvalue.setVisibility(View.VISIBLE);
		convvalue.setVisibility(View.VISIBLE);
		convertbtn.setVisibility(View.VISIBLE);
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
	        if(mDrawerLayout.isDrawerOpen(mDrawerRelative)){
				mDrawerLayout.closeDrawer(mDrawerRelative);
	        }
	        else if(!isOpening){
        		isOpening = true;
        		hideConv();
        		showTimer();
        		mDrawerLayout.openDrawer(mDrawerRelative);
        		new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
		        		isOpening = false;
					}
				}, 1250);
	        }


		}
		if(id==R.id.convert){
			//convert on here
	        // Set the adapter for the list view
	        if(mDrawerLayout.isDrawerOpen(mDrawerRelative)){
				mDrawerLayout.closeDrawer(mDrawerRelative);
	        }
	        else if(!isOpening){
        		isOpening = true;
        		hideTimer();
        		showConv();
        		mDrawerLayout.openDrawer(mDrawerRelative);
        		new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
		        		isOpening = false;
					}
				}, 1250);
	        }

		}

		return super.onOptionsItemSelected(item);
	}
}
