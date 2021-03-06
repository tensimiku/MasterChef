package com.tsmk.cooktimer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import cookmanager.io.RecipeSaver;
import cookmanager.recipe.Page;
import cookmanager.recipe.Recipe;
import cookmanager.recipe.RecipeCategory;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		int totalid=1;

    	RecipeSaver  rs = new RecipeSaver(this);
    	/*
    	Recipe recipe;
    	ArrayList<Page> pList = new ArrayList<Page>();
    	pList.add(new Page("pict0_1", "text0_1", 40));
    	pList.add(new Page("pict0_2", "text0_2", 50));
    	pList.add(new Page("pict0_3", "text0_3", 60));
    	for(int i=0;i<13;i++){
    	recipe = new Recipe("feelsohigh"+i, pList, RecipeCategory.HIGH,totalid++);    	
    	rs.saveRecipe(recipe);
    	}

    	Page[] pageList = new Page[5];
    	pageList[0] = new Page("pict1_1", "text1_1", 40);
    	pageList[1] = new Page("pict1_2", "text1_2", 50);
    	pageList[2] = new Page("pict1_3", "text1_3", 60);
    	pageList[3] = new Page("pict1_4", "text1_4", 70);
    	pageList[4] = new Page("pict1_5", "text1_5", 80);
    	for(int i=0;i<13;i++){
    	recipe = new Recipe("feelsomiddle"+i, pageList, RecipeCategory.MIDDLE ,totalid++);
    	rs.saveRecipe(recipe);
    	}
    	pageList = new Page[4];
    	pageList[0] = new Page("pict2_1", "text2_1", 40);
    	pageList[1] = new Page("pict2_2", "text2_2", 50);
    	pageList[2] = new Page("pict2_3", "text2_3", 60);
    	pageList[3] = new Page("pict2_4", "text2_4", 70);
    	for(int i=0;i<13;i++){
    	recipe = new Recipe("feelsolow"+i, pageList, RecipeCategory.LOW,totalid++);
    	rs.saveRecipe(recipe);
    	}
		*/
    	try {
			copyDataBase("PageReader.db");
			Log.v("copyDB", "dbCopyed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//Write TestCode
    	/*
		try {
	    	InputStream myInput = new FileInputStream("/data/data/com.tsmk.cooktimer/databases/PageReader.db");

	    	File directory = new File(Environment.getExternalStorageDirectory()+"/testdb");
	    	if (!directory.exists()) {
	    	    directory.mkdirs();
	    	}

	    	OutputStream myOutput;
			myOutput = new FileOutputStream(directory.getPath() + "/db.backup");
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer)) > 0) {
	    	    myOutput.write(buffer, 0, length);
	    	}

	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		*/
    	new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),ShowList.class);
				startActivity(i);
				finish();
			}
		}, 1000);


	}
	//dbcopycode

    private void copyDataBase(String dbname) throws IOException {
        // Open your local db as the input stream
        InputStream myInput = getApplicationContext().getAssets().open(dbname);
        // Path to the just created empty db
        String outFileName = getDatabasePath(dbname).getPath();
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
	
	

	
}
