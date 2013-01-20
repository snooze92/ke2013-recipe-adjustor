package keadjustor;

import static ch.lambdaj.Lambda.*;

public class Main {	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java -jar keadjustor.jar KB_PATH RECIPE_PATH");
		}
		else {
			if (KnowledgeBase.INSTANCE.loadFile(args[0])) {
				System.out.println(KnowledgeBase.INSTANCE);
				
				Recipe recipe = new Recipe(args[1]);
				if (recipe.isLoaded()) {
					while(true) {						
						HasIngredient ingredient = null;
						System.out.println(String.format("cal=%s, fats=%s, fibers=%s, gl=%s, proteins=%s", 
								recipe.checkCalories(),								
								recipe.checkFats(),
								recipe.checkFibers(),
								recipe.checkGlycemicLoad(),
								recipe.checkProteins()));
						
						// changing quantities												
						switch (recipe.checkGlycemicLoad()) {
							case 1:
								ingredient = selectMax(recipe.getIngredients(), on(HasIngredient.class).getGlycemicLoad());
								ingredient.setQuantity(ingredient.getQuantity() * 0.8);
								continue;
							case -1:
								ingredient = selectMin(recipe.getIngredients(), on(HasIngredient.class).getGlycemicLoad());
								ingredient.setQuantity(ingredient.getQuantity() * 1.2);
								continue;
							default:
								break;
						}
						
						switch (recipe.checkCalories()) {
							case 1:
								ingredient = selectMax(recipe.getIngredients(), on(HasIngredient.class).getCalories());
								ingredient.setQuantity(ingredient.getQuantity() * 0.8);
								continue;
							case -1:
								ingredient = selectMin(recipe.getIngredients(), on(HasIngredient.class).getCalories());
								ingredient.setQuantity(ingredient.getQuantity() * 1.2);
								continue;
							default:
								break;
						}
						
						switch (recipe.checkFats()) {
							case 1:
								ingredient = selectMax(recipe.getIngredients(), on(HasIngredient.class).getFractionFats());
								ingredient.setQuantity(ingredient.getQuantity() * 0.8);
								continue;
							case -1:
								ingredient = selectMin(recipe.getIngredients(), on(HasIngredient.class).getFractionFats());
								ingredient.setQuantity(ingredient.getQuantity() * 1.2);
								continue;
							default:
								break;
						}	
						
						switch (recipe.checkFibers()) {
							case 1:
								ingredient = selectMax(recipe.getIngredients(), on(HasIngredient.class).getFractionFibers());
								ingredient.setQuantity(ingredient.getQuantity() * 0.8);
								continue;
							case -1:
								ingredient = selectMin(recipe.getIngredients(), on(HasIngredient.class).getFractionFibers());
								ingredient.setQuantity(ingredient.getQuantity() * 1.2);
								continue;
							default:
								break;
						}
						
						switch (recipe.checkProteins()) {
							case 1:
								ingredient = selectMax(recipe.getIngredients(), on(HasIngredient.class).getFractionProteins());
								ingredient.setQuantity(ingredient.getQuantity() * 0.8);
								continue;
							case -1:
								ingredient = selectMin(recipe.getIngredients(), on(HasIngredient.class).getFractionProteins());
								ingredient.setQuantity(ingredient.getQuantity() * 1.2);
								continue;
							default:
								break;
						}
						
						if(ingredient == null) {
							// no quantity changes proposed; break while loop
							break;
						}
					}					
					
					System.out.println(new Recipe(args[1])); // original recipe
					System.out.println(recipe); // new recipe
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

}
