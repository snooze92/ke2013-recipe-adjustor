package keadjustor;

public class Substitution implements FixAction {
	private Ingredient original;
	private Ingredient substitute;
	private double scale;

	public Substitution(Ingredient original, Ingredient substitute, double scale) {
		this.original = original;
		this.substitute = substitute;
		this.scale = scale;
	}
	
	@Override
	public boolean modifyRecipe(Recipe recipe) {
		// TODO: Could be improved
		for (HasIngredient i : recipe.getIngredients()) {
			if (i.getIngredient() == original) {
				i.setIngredient(substitute);
				i.setQuantity(i.getQuantity() * scale);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Replacing %s with %s (scale: %.2f)",
				original.getName(), substitute.getName(), scale);
	}
}
