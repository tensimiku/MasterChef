package com.tsmk.cooktimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cookmanager.io.RecipeLoader;
import cookmanager.recipe.RecipeCategory;

public class RecipeList_tab1 extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.listrecipe, container, false);
		
			ListView lv = (ListView)rootView.findViewById(R.id.foodlist);
			
			RecipeLoader rl = new RecipeLoader(getActivity());
			
			CstmAdapter adapter = new CstmAdapter(getActivity(), R.layout.listres, rl.getRecipeArrayList(RecipeCategory.HIGH, false));
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
		
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent i = new Intent(getActivity(), ShowRecipe.class);
					i.putExtra("selected", arg2);
					i.putExtra("category", 0);
					startActivity(i);
					
					//Toast.makeText(getApplicationContext(),MainActivity.foodlist.get(arg2).getname(), Toast.LENGTH_SHORT).show();
					
					// TODO Auto-generated method stub
					
				}
			});
			return rootView;
	}
}
