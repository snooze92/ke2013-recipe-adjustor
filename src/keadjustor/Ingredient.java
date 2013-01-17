package keadjustor;

public class Ingredient {
	private double quantity;
	
	// Static properties
	private final String name;
	private final double glycemicIndex;
	private final double fractionCarbs;
	private final double fractionFats;
	private final double fractionProteins;
	private final double fractionFibers;
	private final double calories;
	
	// Constructor for KB
	public Ingredient(
			String name,
			double glycemicIndex,
			double fractionCarbs,
			double fractionFats,
			double fractionProteins,
			double fractionFibers,
			double calories) {
		this.quantity = 1.0;
		this.name = name;
		this.glycemicIndex = glycemicIndex;
		this.fractionCarbs = fractionCarbs;
		this.fractionFats = fractionFats;
		this.fractionProteins = fractionProteins;
		this.fractionFibers = fractionFibers;
		this.calories = calories;
	}
	
	// Copy constructor
	public Ingredient(Ingredient other) {
		this.quantity = other.quantity;
		this.name = other.name;
		this.glycemicIndex = other.glycemicIndex;
		this.fractionCarbs = other.fractionCarbs;
		this.fractionFats = other.fractionFats;
		this.fractionProteins = other.fractionProteins;
		this.fractionFibers = other.fractionFibers;
		this.calories = other.calories;
	}
	
	// Getters
	public double getQuantity() {
		return quantity;
	}
	public String getName() {
		return name;
	}
	public double getGlycemicLoad() {
		return glycemicIndex * quantity;
	}
	public double getCarbsLoad() {
		return fractionCarbs * quantity;
	}
	public double getFatsLoad() {
		return fractionFats * quantity;
	}
	public double getProteinsLoad() {
		return fractionProteins * quantity;
	}
	public double getFibersLoad() {
		return fractionFibers * quantity;
	}
	public double getCalories() {
		return calories * quantity;
	}
	
	// Setters
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
