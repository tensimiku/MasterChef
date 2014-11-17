package com.tsmk.cooktimer;

import java.util.ArrayList;

import cookmanager.recipe.Recipe;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 

public class CstmAdapter extends ArrayAdapter<Recipe> {
    private final Context context;
    private final ArrayList<Recipe> data;
    private final int layoutResourceId;

    public CstmAdapter(Context context, int layoutResourceId, ArrayList<Recipe> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView)row.findViewById(R.id.foodname);
            holder.textView2 = (TextView)row.findViewById(R.id.foodstep);
            holder.textView3 = (TextView)row.findViewById(R.id.foodlev);


            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Recipe recipeshow = data.get(position);

    	holder.textView1.setText(recipeshow.getRecipeName());
    	holder.textView2.setText("총 단계:"+Integer.toString(recipeshow.getPageArray().length));
    	switch(recipeshow.getCategory()){
		case HIGH:
	    	holder.textView3.setText(R.string.high);
			break;
		case LOW:
	    	holder.textView3.setText(R.string.low);
			break;
		case MIDDLE:
	    	holder.textView3.setText(R.string.middle);
			break;
		default:
			break;
    	
    	}
        return row;
    }

    static class ViewHolder
    {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }
}