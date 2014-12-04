package cookmanager.recipe;

import android.os.Parcel;
import android.os.Parcelable;

public class Page implements Parcelable
{
	private Picture picture;
	private String text;
	private int timeVal;
	
	public Page(String pict_addr, String text, int timeval)
	{
		this.picture 	= new Picture(pict_addr);
		this.text 		= text;
		this.timeVal 	= timeval;
	}
	
	public String 	getPictureAddress() { return picture.getAddress(); }
	public String 	getText() 			{ return text; }
	public int		getTime() 			{ return timeVal; }
	
	public boolean 	isTimer() { return (timeVal>0)? true : false; }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}