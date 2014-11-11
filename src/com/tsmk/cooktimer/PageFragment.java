package com.tsmk.cooktimer;


import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cookmanager.recipe.Page;

public class PageFragment extends Fragment {
	Page page;
	
	public PageFragment(Page p) {
		// TODO Auto-generated constructor stub
		super();
		this.page = p;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.page_layout, container, false);
			ImageView iv= (ImageView)rootView.findViewById(R.id.recipeimage);
			TextView tv = (TextView)rootView.findViewById(R.id.recipetext);
			Uri path = Uri.parse(page.getPictureAddress());
			File fp = new File(path.toString());
			if(fp.exists()){
				iv.setImageURI(path);
			}
			if(page.getText()!=null){
				tv.setText(page.getText());
			}

			return rootView;
	}
}
