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

public class KnowledgeBase {
	private static KnowledgeBase instance = null;
	private Map<String, Ingredient> ingredients;
	private Map<String, Course> courses;
	
	public static KnowledgeBase getInstance() {
		if (instance == null) {
			instance = new KnowledgeBase();
		}
		return instance;
	}
	
	private KnowledgeBase() {
		clear();
	}

	public void clear() {
		ingredients = new HashMap<String, Ingredient>();
		courses = new HashMap<String, Course>();
	}
	
	public void loadFile(String filepath) {
		// Note: loadFile does NOT clear first
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(filepath));
			
			JSONArray jsonIngredients = (JSONArray) jsonObj.get("ingredients");
			String name;
			for (Object i : jsonIngredients) {
				JSONObject jsonIngredient = (JSONObject) i;
				name = (String) jsonIngredient.get("name");
				ingredients.put(name, new Ingredient(
						name,
						(double) jsonIngredient.get("gl"),
						(double) jsonIngredient.get("carbs"),
						(double) jsonIngredient.get("fats"),
						(double) jsonIngredient.get("proteins"),
						(double) jsonIngredient.get("fibers"),
						(double) jsonIngredient.get("calories")));
			}
			
			JSONArray jsonCourses = (JSONArray) jsonObj.get("courses");
			for (Object course : jsonCourses) {
				JSONObject jsonCourse = (JSONObject) course;
				name = (String) jsonCourse.get("name");
				courses.put(name, new Course(
						name,
						(double) jsonCourse.get("max_gl")));
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
	}
	
	public Ingredient getIngredient(String name) {
		return ingredients.get(name);
	}
	
	public Course getCourse(String name) {
		return courses.get(name);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("KNOWLEDGE BASE:\n\nIngredients:\n");
		
		sb.append("| NAME            |   GI.   |\n");
		for (Ingredient i : ingredients.values()) {
			sb.append(String.format("| %-15s | %7.2f |\n",
					i.getName(),
					i.getGlycemicIndex()));
		}
		
		sb.append("\nCourses:\n");
		sb.append("| NAME       | Max GL. |\n");
		for (Course c : courses.values()) {
			sb.append(String.format("| %-10s | %7.2f |\n",
					c.getName(),
					c.getMaxGlycemicLoad()));
		}
		
		return sb.toString();
	}
}
