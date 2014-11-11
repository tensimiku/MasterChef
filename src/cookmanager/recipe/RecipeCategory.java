package cookmanager.recipe;

public enum RecipeCategory{
	HIGH(0), MIDDLE(1), LOW(2);
	
	private int integer;
	RecipeCategory(int num){
		integer = num;
	}
	
	public int toInteger() { return integer; }
	public static RecipeCategory getCategory(int num)
	{ 
		switch(num)
		{
		case 0:
			return HIGH;
		case 1:
			return MIDDLE;
		case 2:
			return LOW;
		}		
		return null;
	}
}