/*
 * RecipeLoader	�����Ƿδ�!
 * 
 * new RecipeLoader(this);�� �ϸ� �ش� ��Ƽ��Ƽ�� ������ �ִ� ������ �ִ� .db�� ����!
 * 
 *	<��ȯ��>		<�޼ҵ�>		
 *
 * 	Recipe		getRecipe(int recipeIndex)	
 * 		������ recipeIndex�� �ش��ϴ� �༮�� �����Ǹ� ��ȯ!
 * 
 * 	Recipe[]	getRecipeArray()			
 * 		��� ������������ ��ȯ!
 * 
 * 	Recipe[]	getRecipeArray(String searchName)
 * 		�ش� �̸��� �����ǰ� ���Ե� �༮ ��ȯ!
 * 
 * 	Page[]		getFirstPageArray()
 * 		firstPage(�� ó�� ǥ��!) ��ȯ!
 * 
 * 	Page[]		getFirstPageArray(String searchName)
 * 		�ش� �̸��� �������� ǥ���� ��ȯ!
 * 
 * 	��.. �ʿ��ϴٸ�	���� ... �� ���޴�.. ������..
 */
package cookmanager.io;

import java.util.ArrayList;

import cookmanager.recipe.Page;
import cookmanager.recipe.Recipe;
import cookmanager.recipe.RecipeCategory;

import android.content.Context;
import android.database.Cursor;

public final class RecipeLoader
{
	private PageReaderContract contract;
	public RecipeLoader(Context context) 
	{
		contract = new PageReaderContract(context); 
	}
	
	public Recipe getRecipe(int recipeId) 
	{ 		
		ArrayList<Page> 	pageList = new ArrayList<Page>();
		
		String pict, text;
		int timeval;
		
		/* ������ �ε� */
		String selection = RecipeEntry._ID + "=" + String.valueOf(recipeId);
		Cursor rCursor = contract.query(RecipeEntry.TABLE_NAME, selection);
		
		if(!rCursor.moveToFirst())	return null;
		RecipeCategory rCategory = RecipeCategory.getCategory(
				rCursor.getInt(rCursor.getColumnIndex(RecipeEntry.COLUMN_NAME_CATEGORY)));
		String recipeName = rCursor.getString(rCursor.getColumnIndex(RecipeEntry.COLUMN_NAME_RECIPENAME));
		rCursor.close();
		
		/* ������ �ε� */
		selection = PageEntry.COLUMN_NAME_RECIPEID+"="+String.valueOf(recipeId);
		Cursor cursor = contract.query(PageEntry.TABLE_NAME, selection);
		if(cursor.moveToFirst()){
			do
			{
				pict 	= cursor.getString(cursor.getColumnIndex(PageEntry.COLUMN_NAME_PICTURE));
				text 	= cursor.getString(cursor.getColumnIndex(PageEntry.COLUMN_NAME_TEXT));
				timeval = cursor.getInt(cursor.getColumnIndex(PageEntry.COLUMN_NAME_TIMEVAL));
				pageList.add(new Page(pict, text, timeval));
			}while(cursor.moveToNext());			
		}
		return new Recipe(recipeName, pageList, rCategory, recipeId);
	}
	
	public ArrayList<Recipe> getRecipeArrayList(RecipeCategory category, boolean noPage)
	{
		if(category == null) return null;
		
		ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
		ArrayList<Page> 	pageList = new ArrayList<Page>();
		
		String pict, text, recipeName;
		int timeval, recipeId;
		
		String selection = RecipeEntry.COLUMN_NAME_CATEGORY + "=" + String.valueOf(category.toInteger());
		Cursor rCursor = contract.query(RecipeEntry.TABLE_NAME, selection);
		
		Cursor cursor = null;
		if(!rCursor.moveToFirst()) return null;
		do
		{
			recipeName = rCursor.getString(rCursor.getColumnIndex(RecipeEntry.COLUMN_NAME_RECIPENAME));
			recipeId = rCursor.getInt(rCursor.getColumnIndex(RecipeEntry._ID));
			pageList = new ArrayList<Page>();
			if(!noPage)
			{				
				selection = PageEntry.COLUMN_NAME_RECIPEID+"="+String.valueOf(recipeId);
				cursor = contract.query(PageEntry.TABLE_NAME, selection);
				if(cursor.moveToFirst()){
					do
					{
						pict 	= cursor.getString(cursor.getColumnIndex(PageEntry.COLUMN_NAME_PICTURE));
						text 	= cursor.getString(cursor.getColumnIndex(PageEntry.COLUMN_NAME_TEXT));
						timeval = cursor.getInt(cursor.getColumnIndex(PageEntry.COLUMN_NAME_TIMEVAL));
						
						pageList.add(new Page(pict, text, timeval));
					}while(cursor.moveToNext());
				}
			}
			recipeList.add(new Recipe(recipeName, pageList, category, recipeId));
		}while(rCursor.moveToNext());
		
		return recipeList;
	}
	public Recipe[] getRecipeArray(RecipeCategory category, boolean noPage)
	{
		if(category == null) return null;
		
		ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
		ArrayList<Page> 	pageList = new ArrayList<Page>();
		
		String pict, text, recipeName;
		int timeval, recipeId;
		
		String selection = RecipeEntry.COLUMN_NAME_CATEGORY + "=" + String.valueOf(category.toInteger());
		Cursor rCursor = contract.query(RecipeEntry.TABLE_NAME, selection);
		
		Cursor cursor = null;
		if(!rCursor.moveToFirst()) return null;
		do
		{
			recipeName = rCursor.getString(rCursor.getColumnIndex(RecipeEntry.COLUMN_NAME_RECIPENAME));
			recipeId = rCursor.getInt(rCursor.getColumnIndex(RecipeEntry._ID));
			pageList = new ArrayList<Page>();
			if(!noPage)
			{				
				selection = PageEntry.COLUMN_NAME_RECIPEID+"="+String.valueOf(recipeId);
				cursor = contract.query(PageEntry.TABLE_NAME, selection);
				if(cursor.moveToFirst()){
					do
					{
						pict 	= cursor.getString(cursor.getColumnIndex(PageEntry.COLUMN_NAME_PICTURE));
						text 	= cursor.getString(cursor.getColumnIndex(PageEntry.COLUMN_NAME_TEXT));
						timeval = cursor.getInt(cursor.getColumnIndex(PageEntry.COLUMN_NAME_TIMEVAL));
						
						pageList.add(new Page(pict, text, timeval));
					}while(cursor.moveToNext());
				}
			}
			recipeList.add(new Recipe(recipeName, pageList, category, recipeId));
		}while(rCursor.moveToNext());
		
		return recipeList.toArray(new Recipe[0]);
	}
	public Recipe[] getRecipeArray(RecipeCategory category, String searchName, boolean noPage)
	{ 
		if(searchName == null)
			return getRecipeArray(category, noPage);
		else
		{
			Recipe[] recipeArray = getRecipeArray(category, noPage);
			ArrayList<Recipe> recipeArrList = new ArrayList<Recipe>();
			
			for(int i=0; i<recipeArray.length; i++){
				if(recipeArray[i].getPage(0).getText().contains(searchName))
					recipeArrList.add(recipeArray[0]);
			}
			
			return recipeArrList.toArray(new Recipe[0]);
		}
	}
}
