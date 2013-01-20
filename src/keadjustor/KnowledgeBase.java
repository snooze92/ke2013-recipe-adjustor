package keadjustor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public enum KnowledgeBase {
	INSTANCE();
	
	private static final String IngredientNameKey = "name";
	private static final String IngredientGlycemicKey = "glycemic index";
	private static final String IngredientCarbsKey = "carbohydrate";
	private static final String IngredientFatsKey = "saturated fat";
	private static final String IngredientProteinsKey = "protein";
	private static final String IngredientFibersKey = "fiber";
	private static final String IngredientCaloriesKey = "kcal";
	
	private Map<String, Ingredient> ingredients;
	private Map<String, Course> courses;
	private SubstitutionsMap substitutions;
	
	private KnowledgeBase() {
		clear();
	}

	public void clear() {
		ingredients = new HashMap<String, Ingredient>();
		courses = new HashMap<String, Course>();
		substitutions = new SubstitutionsMap();
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
				// Get properties and add..
				ingredients.put(name.toLowerCase(), (new Ingredient.Builder(name))
										.glycemicIndex(getDouble(jsonIngredient, IngredientGlycemicKey))
										.fractionCarbs(getDouble(jsonIngredient, IngredientCarbsKey))
										.fractionFats(getDouble(jsonIngredient, IngredientFatsKey))
										.fractionProteins(getDouble(jsonIngredient, IngredientProteinsKey))
										.fractionFibers(getDouble(jsonIngredient, IngredientFibersKey))
										.calories(getDouble(jsonIngredient, IngredientCaloriesKey))
										.build());
			}
			
			// Load courses
			jsonArray = (JSONArray) jsonObj.get("courses");
			Course.Builder courseProperties;
			JSONArray interval;
			for (Object course : jsonArray) {
				JSONObject jsonCourse = (JSONObject) course;
				name = (String) jsonCourse.get("name");
				// Get all properties
				courseProperties = new Course.Builder(name);
				interval = (JSONArray) jsonCourse.get("gl");
				courseProperties.glycemicLoad((double) interval.get(0), (double) interval.get(1));
				//interval = (JSONArray) jsonCourse.get("carbs");
				//courseProperties.carbs((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("fats");
				courseProperties.fats((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("proteins");
				courseProperties.proteins((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("fibers");
				courseProperties.fibers((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("calories");
				courseProperties.calories((double) interval.get(0), (double) interval.get(1));
				// Add..
				courses.put(name.toLowerCase(), courseProperties.build());
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
				substitutions.put(name.toLowerCase(), new Substitution(original, substitute, (double) jsonSubstitution.get("scale")));
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
	
	public List<Substitution> getSubstitutions(String originalIngredientName) {
		return substitutions.get(originalIngredientName.toLowerCase());
	}
	
	@Override
	public String toString() {
		return String.format("Knowledge Base: %d ingredients, %d courses, %d substitutions",
				ingredients.size(), courses.size(), substitutions.size());
	}
}
