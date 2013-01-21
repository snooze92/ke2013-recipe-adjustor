package keadjustor;

public class HasIngredient {
	private Recipe recipe;
	private double quantity;
	private Ingredient ingredient;

	// Constructor
	public HasIngredient(Recipe recipe, double quantity, Ingredient ingredient) {
		setRecipe(recipe);
		setQuantity(quantity);
		setIngredient(ingredient);
	}

	// Getters
	public Recipe getRecipe() {
		return recipe;
	}	
	public double getQuantity() {
		return quantity;
	}
	public Ingredient getIngredient() {
		return ingredient;
	}
	public double getGlycemicLoad() {
		return ingredient.getGlycemicIndex() * ingredient.getFractionCarbs() * (quantity / recipe.getServings()) / 100;
	}
	public double getFats() {
		return ingredient.getFractionFats() * (quantity / recipe.getServings());
	}
	public double getProteins() {
		return ingredient.getFractionProteins() * (quantity / recipe.getServings());
	}
	public double getFibers() {
		return ingredient.getFractionFibers() * (quantity / recipe.getServings());
	}
	public double getCalories() {
		return ingredient.getCalories() * (quantity / recipe.getServings());
	}
	
	// Setters
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	
	public String toString() {
		return String.format("%s has qty=%.2f", getIngredient().getName(), this.quantity);
	}
}
