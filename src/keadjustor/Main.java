package keadjustor;

public class Main {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java -jar keadjustor.jar KB_PATH RECIPE_PATH");
		}
		else {
			KnowledgeBase.getInstance().loadFile(args[0]);
			System.out.println(KnowledgeBase.getInstance());
			
			System.out.println("=============================\n");
			
			Recipe recipe = new Recipe(args[1]);
			System.out.println(recipe);
		}
	}

}
