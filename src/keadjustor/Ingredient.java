package keadjustor;

public class Ingredient {
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
		this.name = name;
		this.glycemicIndex = glycemicIndex;
		this.fractionCarbs = fractionCarbs;
		this.fractionFats = fractionFats;
		this.fractionProteins = fractionProteins;
		this.fractionFibers = fractionFibers;
		this.calories = calories;
	}
	
	// Getters
	public String getName() {
		return name;
	}
	public double getGlycemicIndex() {
		return glycemicIndex;
	}
	public double getFractionCarbs() {
		return fractionCarbs;
	}
	public double getFractionFats() {
		return fractionFats;
	}
	public double getFractionProteins() {
		return fractionProteins;
	}
	public double getFractionFibers() {
		return fractionFibers;
	}
	public double getCalories() {
		return calories;
	}
}
