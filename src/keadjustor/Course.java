package keadjustor;

public class Course {
	// Constant properties
	private final String name;
	private final Interval glycemicLoad;
	private final Interval carbs;
	private final Interval fats;
	private final Interval proteins;
	private final Interval fibers;
	private final Interval calories;
	
	// Builder
	public static class Builder {
		private final String name;
		private Interval glycemicLoad;
		private Interval carbs;
		private Interval fats;
		private Interval proteins;
		private Interval fibers;
		private Interval calories;
		
		public Builder(String name) {
			this.name = name;
		}
		
		public Builder glycemicLoad(double min, double max) {
			glycemicLoad = new Interval(min, max);
			return this;
		}
		public Builder carbs(double min, double max) {
			carbs = new Interval(min, max);
			return this;
		}
		public Builder fats(double min, double max) {
			fats = new Interval(min, max);
			return this;
		}
		public Builder proteins(double min, double max) {
			proteins = new Interval(min, max);
			return this;
		}
		public Builder fibers(double min, double max) {
			fibers = new Interval(min, max);
			return this;
		}
		public Builder calories(double min, double max) {
			calories = new Interval(min, max);
			return this;
		}
		
		public Course build() {
			return new Course(this);
		}
	}
	
	// Private constructor
	private Course(Builder builder) {
		name = builder.name;
		glycemicLoad = builder.glycemicLoad;
		carbs = builder.carbs;
		fats = builder.fats;
		proteins = builder.proteins;
		fibers = builder.fibers;
		calories = builder.calories;
	}
	
	public String getName() {
		return name;
	}
	
	// Checks
	public double checkGlycemicLoad(double val) {
		return glycemicLoad.check(val);
	}
	public double checkCarbs(double val) {
		return carbs.check(val);
	}
	public double checkFats(double val) {
		return fats.check(val);
	}
	public double checkProteins(double val) {
		return proteins.check(val);
	}
	public double checkFibers(double val) {
		return fibers.check(val);
	}
	public double checkCalories(double val) {
		return calories.check(val);
	}

	@Override
	public String toString() {
		return String.format("Course '%s'", name);
	}
}
