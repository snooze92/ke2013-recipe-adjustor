package keadjustor;

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
