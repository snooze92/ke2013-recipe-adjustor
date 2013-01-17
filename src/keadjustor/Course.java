package keadjustor;

public class Course {
	private String name;
	private double maxGlycemicLoad;
	
	public Course(String name, double maxGlycemicLoad) {
		this.name = name;
		this.maxGlycemicLoad = maxGlycemicLoad;
	}
	
	public String getName() {
		return name;
	}
	
	public double getMaxGlycemicLoad() {
		return maxGlycemicLoad;
	}

	@Override
	public String toString() {
		return String.format("Course '%s'", name);
	}
}
