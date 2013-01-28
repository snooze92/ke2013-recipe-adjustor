package keadjustor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import keadjustor.constraints.MaximumCalories;
import keadjustor.constraints.MaximumGlycemicLoad;
import keadjustor.constraints.MaximumProtein;
import keadjustor.constraints.MaximumSaturatedFat;
import keadjustor.constraints.MinimumCalories;
import keadjustor.constraints.MinimumFiber;
import keadjustor.constraints.MinimumProtein;
import keadjustor.constraints.RecipeConstraint;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public enum KnowledgeBase {
	INSTANCE();
	
	private static final String IngredientNameKey = "name";
	private static final String IngredientTypeKey = "type";
	private static final String IngredientGlycemicKey = "glycemic index";
	private static final String IngredientCarbsKey = "carbohydrate";
	private static final String IngredientFatsKey = "saturated fat";
	private static final String IngredientProteinsKey = "protein";
	private static final String IngredientFibersKey = "fiber";
	private static final String IngredientCaloriesKey = "kcal";
	
	private Map<String, Ingredient> ingredients;
	private Map<String, Course> courses;
	private SubstitutionsMap specificSubstitutions;
	
	private KnowledgeBase() {
		clear();
	}

	public void clear() {
		ingredients = new HashMap<String, Ingredient>();
		courses = new HashMap<String, Course>();
		specificSubstitutions = new SubstitutionsMap();
	}
	
	private static double getDouble(JSONObject jsonObj, String key) {
		double d = 0;
		
		try {
			d = (double) jsonObj.get(key);
		} catch (ClassCastException ex) {
			d = ((Long) jsonObj.get(key)).doubleValue();
		}
		
		return d;
	}
	
	public boolean loadFile(String filepath) {
		// Note: loadFile does NOT clear first
		
		boolean loaded = false;
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(filepath));
			
			// Load ingredients
			JSONArray jsonArray = (JSONArray) jsonObj.get("ingredients");
			String name;
			for (Object i : jsonArray) {
				JSONObject jsonIngredient = (JSONObject) i;
				name = (String) jsonIngredient.get(IngredientNameKey);
				Ingredient newIngredient = (new Ingredient.Builder(name))
						.type((String) jsonIngredient.get(IngredientTypeKey))
						.glycemicIndex(getDouble(jsonIngredient, IngredientGlycemicKey))
						.fractionCarbs(getDouble(jsonIngredient, IngredientCarbsKey))
						.fractionFats(getDouble(jsonIngredient, IngredientFatsKey))
						.fractionProteins(getDouble(jsonIngredient, IngredientProteinsKey))
						.fractionFibers(getDouble(jsonIngredient, IngredientFibersKey))
						.calories(getDouble(jsonIngredient, IngredientCaloriesKey))
						.build();
				// Get properties and add..
				ingredients.put(name.toLowerCase(), newIngredient);
			}
			
			// Load courses
			jsonArray = (JSONArray) jsonObj.get("courses");
			//Course.Builder courseProperties;
			JSONArray interval;
			for (Object rawCourse : jsonArray) {
				JSONObject jsonCourse = (JSONObject) rawCourse;
				name = (String) jsonCourse.get("name");
				// Get all properties
				//courseProperties = new Course.Builder(name);
				
				ArrayList<RecipeConstraint> constraints = new ArrayList<RecipeConstraint>();				
				interval = (JSONArray) jsonCourse.get("gl");
				constraints.add(new MaximumGlycemicLoad((double) interval.get(1)));
				
				interval = (JSONArray) jsonCourse.get("fats");
				constraints.add(new MaximumSaturatedFat((double) interval.get(1))); 
				
				interval = (JSONArray) jsonCourse.get("proteins");
				constraints.add(new MinimumProtein((double) interval.get(0)));
				constraints.add(new MaximumProtein((double) interval.get(1)));
				
				interval = (JSONArray) jsonCourse.get("fibers");
				constraints.add(new MinimumFiber((double) interval.get(0)));
				
				interval = (JSONArray) jsonCourse.get("calories");
				constraints.add(new MinimumCalories((double) interval.get(0)));
				constraints.add(new MaximumCalories((double) interval.get(1)));
				
				Course course = new Course(name, constraints);				
				
				// Add..
				courses.put(name.toLowerCase(), course);
			}
			
			loaded = true;
			
			// Load substitutions
			jsonArray = (JSONArray) jsonObj.get("substitutions");
			Ingredient original, substitute;
			for (Object substitution : jsonArray) {
				JSONObject jsonSubstitution = (JSONObject) substitution;
				name = (String) jsonSubstitution.get("with");
				if ((substitute = getIngredient(name)) == null) {
					System.out.println(String.format("Fatal: The ingredient '%s' is not part of the Knowledge Base.", name));
					loaded = false;
				}
				name = (String) jsonSubstitution.get("replace");
				if ((original = getIngredient(name)) == null) {
					System.out.println(String.format("Fatal: The ingredient '%s' is not part of the Knowledge Base.", name));
					loaded = false;
				}
				specificSubstitutions.put(name.toLowerCase(), new Substitution(original, substitute, (double) jsonSubstitution.get("scale")));
			}
		} catch (FileNotFoundException ex) {
			System.out.println(String.format("File not found (%s)", ex.getMessage()));
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println(String.format("Input error (%s)", ex.getMessage()));
			ex.printStackTrace();
		} catch (ParseException ex) {
			System.out.println(String.format("Parse error (%s)", ex.getMessage()));
			ex.printStackTrace();
		} catch (Exception ex) {
			System.out.println(String.format("Error (%s)", ex.getMessage()));
			ex.printStackTrace();
		}
		return loaded;
	}
	
	public Ingredient getIngredient(String name) {
		return ingredients.get(name.toLowerCase());
	}
	
	public Course getCourse(String name) {
		return courses.get(name.toLowerCase());
	}
	
	public List<Substitution> getSpecificSubstitutions(Ingredient original) {
		List<Substitution> list = specificSubstitutions.get(original.getName().toLowerCase());
		return (list == null) ? new ArrayList<Substitution>() : list;
	}
	
	// TODO: a hash map should be built once for all instead of building this every time
	public List<Substitution> getTypeSubstitutions(Ingredient original) {
		List<Substitution> result = new ArrayList<Substitution>();
		
		if (original.getType().length() > 0) {
			for (Ingredient substitute : ingredients.values()) {
				if (substitute.getType().equals(original.getType()) && substitute != original) {
					result.add(new Substitution(original, substitute, 1.0));
				}
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Knowledge Base: %d ingredients, %d courses, %d specific substitutions",
				ingredients.size(), courses.size(), specificSubstitutions.size());
	}
}
