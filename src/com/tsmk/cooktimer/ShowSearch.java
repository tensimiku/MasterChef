package com.tsmk.cooktimer;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cookmanager.io.RecipeLoader;
import cookmanager.recipe.Recipe;
import cookmanager.recipe.RecipeCategory;

public class ShowSearch extends ActionBarActivity {

	private String query;
	private RecipeLoader rl;
	private ArrayList<Recipe> ra;
	private android.support.v7.app.ActionBar actionbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_act);
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	    Intent intent = getIntent();
	    
	    
	    
	    rl = new RecipeLoader(getApplicationContext());
	    ra = new ArrayList<Recipe>();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      query = intent.getStringExtra(SearchManager.QUERY);
	      actionbar.setTitle(query);
	      Recipe[] tmp = rl.getRecipeArray(RecipeCategory.HIGH, query, false);
	      for(int i=0;i<tmp.length;i++){
	    	  ra.add(tmp[i]);
	      }
	      tmp = rl.getRecipeArray(RecipeCategory.MIDDLE, query, false);
	      for(int i=0;i<tmp.length;i++){
	    	  ra.add(tmp[i]);
	      }
	      tmp = rl.getRecipeArray(RecipeCategory.LOW, query, false);
	      for(int i=0;i<tmp.length;i++){
	    	  ra.add(tmp[i]);
	      }
	    }
		ListView lv = (ListView)findViewById(R.id.searchlist);
		CstmAdapter adapter = new CstmAdapter(this, R.layout.listres, ra);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int selrcp = ra.get(arg2).getRecipeId();
				Intent i = new Intent(getApplicationContext(), ShowRecipe.class);
				i.putExtra("selected", selrcp);
				startActivity(i);
				
				//Toast.makeText(getApplicationContext(),MainActivity.foodlist.get(arg2).getname(), Toast.LENGTH_SHORT).show();
				
				// TODO Auto-generated method stub
				
			}
		});

	}

	
}
