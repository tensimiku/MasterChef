package cookmanager.io;

import cookmanager.recipe.Page;
import cookmanager.recipe.Recipe;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

abstract class PageEntry implements BaseColumns
{
	public static final String TABLE_NAME = "page";
	public static final String COLUMN_NAME_PICTURE 	= "picture";
	public static final String COLUMN_NAME_TEXT 	= "text";
	public static final String COLUMN_NAME_TIMEVAL 	= "timeval";
	public static final String COLUMN_NAME_PAGEID	= "pageid";		
	public static final String COLUMN_NAME_RECIPEID = "recipeid";
}
abstract class RecipeEntry implements BaseColumns
{
	public static final String TABLE_NAME = "recipe";
	public static final String COLUMN_NAME_CATEGORY = "category";
	public static final String COLUMN_NAME_RECIPENAME = "name";
}

final class PageReaderContract
{	
	private static PageReaderDbHelper mDbHelper = null;
	
	public PageReaderContract(Context context){
		if(mDbHelper == null){
			mDbHelper = new PageReaderDbHelper(context);
			mDbHelper.onUpgrade(mDbHelper.getWritableDatabase(), 1, 1);
		}
	}	
	
	private static final String TEXT_TYPE = " TEXT ";
	private static final String INTEGER_TYPE = " INTEGER ";
	private static final String COMMA_SEP = " , ";
	private static final String SQL_CREATE_PAGETABLE =
			"CREATE TABLE " + PageEntry.TABLE_NAME + " (" +
			PageEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
			PageEntry.COLUMN_NAME_PAGEID + INTEGER_TYPE + COMMA_SEP +
			PageEntry.COLUMN_NAME_PICTURE + TEXT_TYPE + COMMA_SEP +
			PageEntry.COLUMN_NAME_TEXT 	  + TEXT_TYPE + COMMA_SEP +
			PageEntry.COLUMN_NAME_TIMEVAL + INTEGER_TYPE + COMMA_SEP +			
			PageEntry.COLUMN_NAME_RECIPEID + INTEGER_TYPE + COMMA_SEP +			
			"FOREIGN KEY(" + PageEntry.COLUMN_NAME_RECIPEID + ")"
			+ " REFERENCES " + RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + ")"
			+ " ON DELETE CASCADE " +
			" )";
	private static final String SQL_CREATE_RECIPETABLE =
			"CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
			RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
			RecipeEntry.COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP +
			RecipeEntry.COLUMN_NAME_RECIPENAME + TEXT_TYPE +
			" )";
	
	private static final String SQL_DELETE_PAGETABLE =
			"DROP TABLE IF EXISTS " + PageEntry.TABLE_NAME;
	private static final String SQL_DELETE_RECIPETABLE =
			"DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME;
	
	public class PageReaderDbHelper extends SQLiteOpenHelper
	{
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "PageReader.db";
		
		public PageReaderDbHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_RECIPETABLE);
			db.execSQL(SQL_CREATE_PAGETABLE);			
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL(SQL_DELETE_RECIPETABLE);
			db.execSQL(SQL_DELETE_PAGETABLE);			
			onCreate(db);
		}
		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
			onUpgrade(db, oldVersion, newVersion);
		}
	}
	
	
	public void insert(Recipe recipe, boolean overwrite)
	{    	
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		String selection = RecipeEntry._ID + " = " + String.valueOf(recipe.getRecipeId());
		Cursor c = query(RecipeEntry.TABLE_NAME, selection);
		
		if(c.moveToFirst() && overwrite) // 존재하면
		{
			delete(RecipeEntry.TABLE_NAME, selection);
		}
		/* 레시피 생성 */
		ContentValues values = new ContentValues();
    	values.put(RecipeEntry.COLUMN_NAME_CATEGORY,recipe.getCategory().toInteger());
    	values.put(RecipeEntry.COLUMN_NAME_RECIPENAME, recipe.getRecipeName());
    	
    	long recipeId = db.insert(RecipeEntry.TABLE_NAME, "null", values);
    	
		
		/* 페이지 추가 */		
		Page[] pageArray = recipe.getPageArray();
		for(int pIdx=0; pIdx<pageArray.length; pIdx++)
		{
			values = new ContentValues();
	    	values.put(PageEntry.COLUMN_NAME_PAGEID, pIdx);
	    	values.put(PageEntry.COLUMN_NAME_PICTURE, pageArray[pIdx].getPictureAddress());
	    	values.put(PageEntry.COLUMN_NAME_TEXT, pageArray[pIdx].getText());
	    	values.put(PageEntry.COLUMN_NAME_TIMEVAL, pageArray[pIdx].getTime());
	    	values.put(PageEntry.COLUMN_NAME_RECIPEID, recipeId);
	 
	    	db.insert(PageEntry.TABLE_NAME,	"null", values);
		}
	}
	
	public Cursor query(String table, String selection)
	{
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		if(table.equals(RecipeEntry.TABLE_NAME))
		{
			String[] projection = {
					RecipeEntry._ID,				
					RecipeEntry.COLUMN_NAME_CATEGORY,
					RecipeEntry.COLUMN_NAME_RECIPENAME
			};
			
			String sortOrder =
					RecipeEntry._ID + " ASC";
			
			return db.query(
					RecipeEntry.TABLE_NAME,
					projection, 
					selection,
					null, null, null, sortOrder);
		}
		else if(table.equals(PageEntry.TABLE_NAME))
		{
			String[] projection = {
					PageEntry._ID,				
					PageEntry.COLUMN_NAME_PAGEID,
					PageEntry.COLUMN_NAME_PICTURE,
					PageEntry.COLUMN_NAME_TEXT,
					PageEntry.COLUMN_NAME_TIMEVAL,
					PageEntry.COLUMN_NAME_RECIPEID,
			};	
			
			String sortOrder =
					PageEntry.COLUMN_NAME_PAGEID + " ASC";
			
			return db.query(
					PageEntry.TABLE_NAME,
					projection, 
					selection,
					null, null, null, sortOrder);
		}
		else
			return null;
	}
	
	public void delete(String table, String selection)
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(table, selection, null);
	}
}