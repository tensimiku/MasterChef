package cookmanager.recipe;

import java.util.ArrayList;
import java.util.Iterator;


public class Recipe
{
	private ArrayList<Page> pageList;
	private RecipeCategory category;
	private int recipeId;
	private String recipeName;
	
	public Recipe(String name, Page[] pageArr, RecipeCategory category){
		this(name, pageArr, category,-1);
	}
	
	
	public Recipe(String name, Page[] pageArr, RecipeCategory category, int id){
		pageList = new ArrayList<Page>();
		for(int i=0; i<pageArr.length; i++)
			pageList.add(pageArr[i]);
		
		this.category = category;
		this.recipeId = id;
		this.recipeName = name;
	}
	public Recipe(String name, ArrayList<Page> pageList, RecipeCategory category){
		this.pageList = pageList;
		this.category = category;	
		this.recipeId = -1;
		this.recipeName = name;
	}
	public Recipe(String name, ArrayList<Page> pageList, RecipeCategory category, int id){
		this.pageList = pageList;
		this.category = category;
		this.recipeId = id;
		this.recipeName = name;
	}
	
	public final Page			getPage(int pageIndex) 	{ return pageList.get(pageIndex); }
	public final Page[] 		getPageArray()			{ return pageList.toArray(new Page[0]); }
	public Iterator<Page> 		getIterator()			{ return pageList.iterator(); }
	public int					getLength()				{ return pageList.size(); }
	public RecipeCategory		getCategory()			{ return category; }
	public int					getRecipeId()			{ return recipeId; }
	public String				getRecipeName()			{ return recipeName; }
	public boolean 				isTimer(int pageIndex) 	{ return pageList.get(pageIndex).isTimer(); }
}