package keadjustor;

import java.util.HashMap;
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
	
	private Map<String, Ingredient> ingredients;
	private Map<String, Course> courses;
	private Map<String, Substitution> substitutions;
	
	private KnowledgeBase() {
		clear();
	}

	public void clear() {
		ingredients = new HashMap<String, Ingredient>();
		courses = new HashMap<String, Course>();
		substitutions = new HashMap<String, Substitution>();
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
				name = (String) jsonIngredient.get("name");
				// Get properties and add..
				ingredients.put(name, new Ingredient.Builder(name)
										.glycemicIndex((double) jsonIngredient.get("gl"))
										.fractionCarbs((double) jsonIngredient.get("carbs"))
										.fractionFats((double) jsonIngredient.get("fats"))
										.fractionProteins((double) jsonIngredient.get("proteins"))
										.fractionFibers((double) jsonIngredient.get("fibers"))
										.calories((double) jsonIngredient.get("calories"))
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
				interval = (JSONArray) jsonCourse.get("carbs");
				courseProperties.carbs((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("fats");
				courseProperties.fats((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("proteins");
				courseProperties.proteins((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("fibers");
				courseProperties.fibers((double) interval.get(0), (double) interval.get(1));
				interval = (JSONArray) jsonCourse.get("calories");
				courseProperties.calories((double) interval.get(0), (double) interval.get(1));
				// Add..
				courses.put(name, courseProperties.build());
			}
			
			loaded = true;
			
			// Load substitutions
			jsonArray = (JSONArray) jsonObj.get("substitutions");
			Ingredient original, substitute;
			for (Object substitution : jsonArray) {
				JSONObject jsonSubstitution = (JSONObject) substitution;
				name = (String) jsonSubstitution.get("with");
				if ((substitute = ingredients.get(name)) == null) {
					System.out.println(String.format("Fatal: The ingredient '%s' is not part of the Knowledge Base.", name));
					loaded = false;
				}
				name = (String) jsonSubstitution.get("replace");
				if ((original = ingredients.get(name)) == null) {
					System.out.println(String.format("Fatal: The ingredient '%s' is not part of the Knowledge Base.", name));
					loaded = false;
				}
				substitutions.put(name, new Substitution(original, substitute, (double) jsonSubstitution.get("scale")));
			}
		} catch (FileNotFoundException ex) {
			System.out.println(String.format("File not found (%s)", ex.getMessage()));
		} catch (IOException ex) {
			System.out.println(String.format("Input error (%s)", ex.getMessage()));
		} catch (ParseException ex) {
			System.out.println(String.format("Parse error (%s)", ex.getMessage()));
		} catch (Exception ex) {
			System.out.println(String.format("Error (%s)", ex.getMessage()));
		}
		return loaded;
	}
	
	public Ingredient getIngredient(String name) {
		return ingredients.get(name);
	}
	
	public Course getCourse(String name) {
		return courses.get(name);
	}
	
	@Override
	public String toString() {
		return String.format("Knowledge Base: %d ingredients, %d courses, %d substitutions",
				ingredients.size(), courses.size(), substitutions.size());
	}
}
