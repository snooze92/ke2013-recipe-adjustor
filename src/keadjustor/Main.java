package keadjustor;

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
					System.out.println(String.format("\n[ORIGINAL] %s", originalRecipe));
					
					Recipe newRecipe, bestRecipe, currentRecipe = originalRecipe;
					double newEval, bestEval, currentEval = currentRecipe.evaluate();
					FixAction bestFix;
					// Adjustment loop:
					while(currentEval > 0.0) {
						bestRecipe = currentRecipe;
						bestEval = currentEval;
						bestFix = null;

						// Try specific substitutions
						for (HasIngredient i : currentRecipe.getIngredients()) {
							for (FixAction fix : KnowledgeBase.INSTANCE.getSpecificSubstitutions(i.getIngredient())) {
								newRecipe = new Recipe(currentRecipe);
								fix.modifyRecipe(newRecipe);
								newEval = newRecipe.evaluate();
								
								if (newEval < bestEval) {
									// Best FixAction so far
									bestEval = newEval;
									bestRecipe = newRecipe;
									bestFix = fix;
								}
							}
						}
						
						if (bestFix == null) {
							// Try type substitutions
							for (HasIngredient i : currentRecipe.getIngredients()) {
								for (FixAction fix : KnowledgeBase.INSTANCE.getTypeSubstitutions(i.getIngredient())) {
									newRecipe = new Recipe(currentRecipe);
									fix.modifyRecipe(newRecipe);
									newEval = newRecipe.evaluate();
									
									if (newEval < bestEval) {
										// Best FixAction so far
										bestEval = newEval;
										bestRecipe = newRecipe;
										bestFix = fix;
									}
								}
							}
						}
						
						if (bestFix == null) {
							// Try quantity adjustments
							for (FixAction fix : currentRecipe.getPossibleAdjustments()) {
								newRecipe = new Recipe(currentRecipe);
								fix.modifyRecipe(newRecipe);
								newEval = newRecipe.evaluate();
								
								if (newEval < bestEval) {
									// Best FixAction so far
									bestEval = newEval;
									bestRecipe = newRecipe;
									bestFix = fix;
								}
							}
						}
						
						// Everything has been tried
						if (bestFix == null) {
							System.out.println("[LOG] No more possible fix action.");
							break;
						}
						else {
							// Keep best adjustment and reiterate
							System.out.println(String.format("[LOG] %s => eval from %.2f down to %.2f",
									bestFix, currentEval, bestEval));
							currentRecipe = bestRecipe;
							currentEval = bestEval;
							
							// System.out.println(String.format("\nNEW: %s", currentRecipe));
						}
					}
					
					System.out.println(String.format("\n[ADJUSTED] %s", currentRecipe));
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
