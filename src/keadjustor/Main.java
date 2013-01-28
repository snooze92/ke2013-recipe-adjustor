package keadjustor;
import static ch.lambdaj.Lambda.*;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import keadjustor.constraints.MaximumCalories;
import keadjustor.constraints.MaximumGlycemicLoad;
import keadjustor.constraints.MaximumProtein;
import keadjustor.constraints.MaximumSaturatedFat;
import keadjustor.constraints.MinimumCalories;
import keadjustor.constraints.MinimumFiber;
import keadjustor.constraints.MinimumProtein;
import keadjustor.constraints.RecipeConstraint;

public class Main {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java -jar keadjustor.jar KB_PATH RECIPE_PATH");
		}
		else {
			if (KnowledgeBase.INSTANCE.loadFile(args[0])) {
				System.out.println(String.format("[LOADED] %s", KnowledgeBase.INSTANCE));	
				
				Recipe originalRecipe = new Recipe(args[1]);
				
				if (originalRecipe.isLoaded()) {
					// Everything was well loaded
					System.out.println(String.format("\n[LOADED] %s", originalRecipe));
					
					Recipe currentRecipe = new Recipe(originalRecipe);
					RecipeConstraint violation; 
					
					// while currentRecipe does not fire a violation
					int i = 0;
					while((violation = currentRecipe.verify()) != null) {
						if (i == 5000) break; // exit long loops 
						i++;
						System.out.println("[LOG] ---- PCM cycle:");
						System.out.println("[LOG] Violation: " + violation);
						System.out.println(String.format("[LOG] GL=%.2f, Calories=%.2f, Fats=%.2f, Proteins=%.2f, Fibers=%.2f",
								currentRecipe.getGlycemicLoad(),
								currentRecipe.getCalories(),
								currentRecipe.getFats(),
								currentRecipe.getProteins(),
								currentRecipe.getFibers()));
						
						// get all possible fixactions 
						ArrayList<FixAction> fixActions = critique(currentRecipe);
						// select the best fixaction
						FixAction bestFixAction = select_fix_action(currentRecipe, violation, fixActions);
						System.out.println("[LOG] " + bestFixAction);
						System.out.println("[LOG] ---- END PCM cycle:");
						// modify recipe with best fixaction and continue						
						bestFixAction.modify(currentRecipe);
					}
					
					System.out.println(String.format("\n[ORIGINAL] %s", originalRecipe));
					System.out.println(String.format("[ADJUSTED] %s", currentRecipe));
				}
				else {
					System.out.println("Error(s) while loading the Recipe.");
				}
			}
			else {
				System.out.println("Error(s) while loading the Knowledge Base.");
			}
		}
	}
	public static ArrayList<FixAction> critique(Recipe recipe) {
		ArrayList<FixAction> fixActions = new ArrayList<FixAction>();				
		
		for(HasIngredient i : recipe.getIngredients()) {
			for (FixAction fix : KnowledgeBase.INSTANCE.getSpecificSubstitutions(i.getIngredient())) {
				fixActions.add(fix);
			}
			for (FixAction fix : KnowledgeBase.INSTANCE.getTypeSubstitutions(i.getIngredient())) {
				fixActions.add(fix);
			}
		}
		for (FixAction fix : recipe.getPossibleAdjustments()) {
			fixActions.add(fix);
		}
		return fixActions;
	}
	
	public static FixAction select_fix_action(Recipe recipe, RecipeConstraint violation, ArrayList<FixAction> fixActions)
	{
		Map<Recipe, FixAction> recipeFixes = new HashMap<Recipe, FixAction>();
		for(FixAction fa : fixActions) {
			Recipe modifiedRecipe = new Recipe(recipe);
			fa.modify(modifiedRecipe);
			recipeFixes.put(modifiedRecipe, fa);
		}		
		
		if(violation instanceof MaximumGlycemicLoad)
			recipe = selectMin(recipeFixes.keySet(), on(Recipe.class).getGlycemicLoad());
		if(violation instanceof MaximumCalories)
			recipe = selectMin(recipeFixes.keySet(), on(Recipe.class).getCalories());
		if(violation instanceof MinimumCalories)
			recipe = selectMax(recipeFixes.keySet(), on(Recipe.class).getCalories());
		if(violation instanceof MaximumSaturatedFat)
			recipe = selectMin(recipeFixes.keySet(), on(Recipe.class).getFats());
		if(violation instanceof MaximumProtein)
			recipe = selectMin(recipeFixes.keySet(), on(Recipe.class).getProteins());
		if(violation instanceof MinimumProtein)
			recipe = selectMax(recipeFixes.keySet(), on(Recipe.class).getProteins());
		if(violation instanceof MinimumFiber)
			recipe = selectMax(recipeFixes.keySet(), on(Recipe.class).getFibers());
					
		// return best fix
		return recipeFixes.get(recipe);
	}
}