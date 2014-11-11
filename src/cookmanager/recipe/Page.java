package cookmanager.recipe;

public class Page
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
}