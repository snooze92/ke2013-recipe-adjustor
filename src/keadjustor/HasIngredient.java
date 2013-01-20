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
	public double getFractionCarbs() {
		return quantity * ingredient.getFractionCarbs() / recipe.getServings();
	}
	public double getFractionFats() {
		return quantity * ingredient.getFractionFats() / recipe.getServings();
	}
	public double getFractionProteins() {
		return quantity * ingredient.getFractionProteins() / recipe.getServings();
	}
	public double getFractionFibers() {
		return quantity * ingredient.getFractionFibers() / recipe.getServings();
	}
	public double getCalories() {
		return quantity * ingredient.getCalories() / recipe.getServings();
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
		return String.format("%s with qty %f", getIngredient().getName(), getQuantity());
	}
}
