package keadjustor;

public class QuantityAdjustment implements FixAction {
	public static final double adjustmentFactor = 0.05;
	
	private Ingredient ingredient;
	private double scale, prevQuantity, newQuantity;

	public QuantityAdjustment(Ingredient ingredient, double scale) {
		this.ingredient = ingredient;
		this.scale = scale;
		
		// toString should not be called with these two values
		this.prevQuantity = -1.0;
		this.newQuantity = -1.0;
	}

	@Override
	public boolean modify(Recipe recipe) {
		// TODO: Could be improved
		for (HasIngredient i : recipe.getIngredients()) {
			if (i.getIngredient() == ingredient) {
				prevQuantity = i.getQuantity();
				newQuantity = prevQuantity * scale;
				i.setQuantity(newQuantity);
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("Adjusting %s quantity from %.2f to %.2f",
				ingredient.getName(), prevQuantity, newQuantity);
	}
}
