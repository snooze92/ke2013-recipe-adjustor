package keadjustor;

import java.util.ArrayList;
import keadjustor.constraints.RecipeConstraint;

public class Course {
	// Constant properties
	private final String name;
	private final ArrayList<RecipeConstraint> constraints;
	
	public Course(String name, ArrayList<RecipeConstraint> constraints) {
		this.name = name;
		this.constraints = constraints;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<RecipeConstraint> getConstraints() {
		return constraints;
	}
	
	@Override
	public String toString() {
		return String.format("Course '%s'", name);
	}
}
