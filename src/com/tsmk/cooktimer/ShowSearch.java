package com.tsmk.cooktimer;

import java.util.ArrayList;

import cookmanager.io.RecipeLoader;
import cookmanager.recipe.Recipe;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class ShowSearch extends ActionBarActivity {

	private RecipeLoader rl;
	private ArrayList<Recipe> ra;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_act);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    Intent intent = getIntent();
	    rl = new RecipeLoader(getApplicationContext());
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      ra = rl.getRecipeArrayList(null, query, false);
	    }
	}

	
}
