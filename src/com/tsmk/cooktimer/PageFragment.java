package com.tsmk.cooktimer;


import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cookmanager.recipe.Page;

public class PageFragment extends Fragment {
	
	public PageFragment(){
		super();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		Page page = getArguments().getParcelable("page");
		View rootView = inflater.inflate(R.layout.page_layout, container, false);
		ImageView iv= (ImageView)rootView.findViewById(R.id.recipeimage);
		TextView tv = (TextView)rootView.findViewById(R.id.recipetext);
		String path = page.getPictureAddress();
		
		if(path!=null){
	        try {
				InputStream myInput = getActivity().getAssets().open(path);
				Bitmap bmp = BitmapFactory.decodeStream(myInput);
				iv.setImageBitmap(bmp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		if(page.getText()!=null){
			tv.setText(page.getText());
		}

		return rootView;
	}
}
