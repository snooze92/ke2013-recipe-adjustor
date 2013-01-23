package keadjustor;

public class Ingredient {
	// Constant properties
	private final String name;
	private final String type;
	private final double glycemicIndex;
	private final double fractionCarbs;
	private final double fractionFats;
	private final double fractionProteins;
	private final double fractionFibers;
	private final double calories;
	
	// Builder
	public static class Builder {
		private final String name;
		private String type;
		private double glycemicIndex;
		private double fractionCarbs;
		private double fractionFats;
		private double fractionProteins;
		private double fractionFibers;
		private double calories;
		
		public Builder(String name) {
			this.name = name;
		}
		
		public Builder type(String val) {
			type = val;
			return this;
		}
		public Builder glycemicIndex(double val) {
			glycemicIndex = val;
			return this;
		}
		public Builder fractionCarbs(double val) {
			fractionCarbs = val;
			return this;
		}
		public Builder fractionFats(double val) {
			fractionFats = val;
			return this;
		}
		public Builder fractionProteins(double val) {
			fractionProteins = val;
			return this;
		}
		public Builder fractionFibers(double val) {
			fractionFibers = val;
			return this;
		}
		public Builder calories(double val) {
			calories = val;
			return this;
		}
		
		public Ingredient build() {
			return new Ingredient(this);
		}
	}
	
	// Private constructor
	private Ingredient(Builder builder) {
		name = builder.name;
		type = builder.type;
		glycemicIndex = builder.glycemicIndex;
		fractionCarbs = builder.fractionCarbs;
		fractionFats = builder.fractionFats;
		fractionProteins = builder.fractionProteins;
		fractionFibers = builder.fractionFibers;
		calories = builder.calories;
	}
	
	// Getters
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
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
