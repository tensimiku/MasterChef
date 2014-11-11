package cookmanager.io;

import android.content.Context;
import cookmanager.recipe.Recipe;

public class RecipeSaver {
	private PageReaderContract contract;
	public RecipeSaver(Context context){
		contract = new PageReaderContract(context); 
	}
	
	public void saveRecipe(Recipe recipe){ 
		contract.insert(recipe, true);
	}
}