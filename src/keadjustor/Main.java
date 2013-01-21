package keadjustor;

public class Main {
	private static double adjustmentFactor = 0.01;
	public static void printIngredient(String constraint, HasIngredient ingredient)
	{
		System.out.println(String.format("Constraint %s violated, ingredient adjusted -> %s", constraint, ingredient));
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java -jar keadjustor.jar KB_PATH RECIPE_PATH");
		}
		else {
			if (KnowledgeBase.INSTANCE.loadFile(args[0])) {
				System.out.println(KnowledgeBase.INSTANCE);				
				Recipe recipe = new Recipe(args[1]);
				double newEvaluation, evaluation;
				if (recipe.isLoaded()) {
					while((evaluation = recipe.evaluate()) > 0.0) {												
						boolean increase = false;
						HasIngredient selected = null;						
						for(HasIngredient hi : recipe.getIngredients()) {
							// ingredient adjustment evaluation
							
							double originalQuantity = hi.getQuantity();
							
							// decrease quantity and evaluate recipe
							hi.setQuantity(originalQuantity * (1 - adjustmentFactor));
							newEvaluation = recipe.evaluate();
							
							// if new evaluation of decreased ingredient is better than 
							// previous evaluation, set the ingredient to adjust to selected. for now..
							if(evaluation > newEvaluation) {
								increase = false;
								evaluation = newEvaluation;
								selected = hi;
							}
							
							// increase quantity and evaluate recipe
							hi.setQuantity(originalQuantity * (1 + adjustmentFactor));
							newEvaluation = recipe.evaluate();
							
							// if new evaluation of increased ingredient better than 
							// previous evaluation set the ingredient to adjust to selected. for now..
							if(evaluation > newEvaluation) {
								increase = true;
								evaluation = newEvaluation;
								selected = hi;
							}							
							
							// set the quantity to the original quantity for evaluation of the recipe
							// in the next iteration of the current for loop
							hi.setQuantity(originalQuantity);
						}
						
						// we're out of the previous for loop! increase or decrease the ingredient based
						// on the best evaluation score
						if(selected != null)
						{
							if(increase)
							{
								selected.setQuantity(selected.getQuantity() * (1 + adjustmentFactor));
							}else{
								selected.setQuantity(selected.getQuantity() * (1 - adjustmentFactor));
							}
							
							System.out.println("Adjusting ingredient: " + selected);
						}else{
							// no ingredient selected during evaluation, exit!
							System.out.println("Could not further satisfy constraints..");
							System.out.println();
							break;
						}
					}					

					System.out.println("Original recipe:");
					System.out.println(new Recipe(args[1]));
					System.out.println("Adjusted recipe:");
					System.out.println(recipe);
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
