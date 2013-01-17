package keadjustor;

public class Substitution {
	private Ingredient original;
	private Ingredient substitute;
	private double scale;

	public Substitution(Ingredient original, Ingredient substitute, double scale) {
		this.original = original;
		this.substitute = substitute;
		this.scale = scale;
	}
	
	public boolean modifyRecipe(Recipe recipe) {
		// Note: implementing equals and gethash for HasIngredient would
		// allow to use a more performant datastructure (e.g. TreeSet)
		for (HasIngredient i : recipe.getIngredients()) {
			if (i.getIngredient() == original) {
				i.setIngredient(substitute);
				i.setQuantity(i.getQuantity() * scale);
				return true;
			}
		}
		return false;
	}
}
