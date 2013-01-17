package keadjustor;

public class HasIngredient {
	private double quantity;
	private Ingredient ingredient;

	// Constructor
	public HasIngredient(double quantity, Ingredient ingredient) {
		setQuantity(quantity);
		setIngredient(ingredient);
	}

	// Getters
	public double getQuantity() {
		return quantity;
	}
	public Ingredient getIngredient() {
		return ingredient;
	}
	public double getGlycemicLoad() {
		return quantity * ingredient.getGlycemicIndex();
	}
	public double getFractionCarbs() {
		return quantity * ingredient.getFractionCarbs();
	}
	public double getFractionFats() {
		return quantity * ingredient.getFractionFats();
	}
	public double getFractionProteins() {
		return quantity * ingredient.getFractionProteins();
	}
	public double getFractionFibers() {
		return quantity * ingredient.getFractionFibers();
	}
	public double getCalories() {
		return quantity * ingredient.getCalories();
	}
	
	// Setters
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
}
