package keadjustor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Recipe {
	private String name;
	private int servings;
	private Course course;
	
	private boolean loaded;
	
	private List<Ingredient> ingredients;
	
	// Construction from JSON file
	public Recipe(String filepath) {
		this.loaded = true;
		this.ingredients = new ArrayList<Ingredient>();
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(filepath));
			
			name = (String) jsonObj.get("name");
			servings = (int) (long) jsonObj.get("servings");
			String courseName = (String) jsonObj.get("course");
			if ((course = KnowledgeBase.getInstance().getCourse(courseName)) == null) {
				System.out.println(String.format("Fatal: The course '%s' is not part of the Knowledge Base.", courseName));
				this.loaded = false;
			}
			
			// Basic infos loaded well, load ingredients
			String ingredientName;
			Ingredient ingredient;
			if (this.loaded) {
				JSONArray jsonIngredients = (JSONArray) jsonObj.get("ingredients");
				for (Object i : jsonIngredients) {
					JSONObject jsonIngredient = (JSONObject) i;
					ingredientName = (String) jsonIngredient.get("name");
					if ((ingredient = KnowledgeBase.getInstance().getIngredient(ingredientName)) == null) {
						System.out.println(String.format("Fatal: The ingredient '%s' is not part of the Knowledge Base.", ingredientName));
						this.loaded = false;
					}
					else {
						// Copy to keep the knowledge base reference clean
						ingredient = new Ingredient(ingredient);
						ingredient.setQuantity((double) jsonIngredient.get("quantity"));
						ingredients.add(ingredient);
					}
				}
			}
			
			
		} catch (FileNotFoundException ex) {
			System.out.println(String.format("File not found (%s)", ex.getMessage()));
			this.loaded = false;
		} catch (IOException ex) {
			System.out.println(String.format("Input error (%s)", ex.getMessage()));
			this.loaded = false;
		} catch (ParseException ex) {
			System.out.println(String.format("Parse error (%s)", ex.getMessage()));
			this.loaded = false;
		} catch (Exception ex) {
			System.out.println(String.format("Error (%s)", ex.getMessage()));
			this.loaded = false;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public double getGlycemicLoad() {
		double sum = 0;
		
		for(Ingredient i : ingredients) {
			sum += i.getGlycemicLoad();
		}
		
		return (sum / servings);
	}
	
	public boolean checkGlycemicLoad() {
		return getGlycemicLoad() < course.getMaxGlycemicLoad();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(String.format("RECIPE:\n\n%s for %d (%s)\n\nIngredients:\n", name, servings, course));
		
		for (Ingredient i : ingredients) {
			sb.append(String.format("%8.1f %s\n", i.getQuantity(), i.getName()));
		}
		
		return sb.toString();
	}
}
