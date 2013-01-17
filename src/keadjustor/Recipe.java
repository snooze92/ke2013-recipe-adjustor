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
	
	private List<HasIngredient> ingredients;
	
	// Construction from JSON file
	public Recipe(String filepath) {
		this.loaded = true;
		this.ingredients = new ArrayList<HasIngredient>();
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(filepath));
			
			name = (String) jsonObj.get("name");
			servings = (int) (long) jsonObj.get("servings");
			String courseName = (String) jsonObj.get("course");
			if ((course = KnowledgeBase.INSTANCE.getCourse(courseName)) == null) {
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
					if ((ingredient = KnowledgeBase.INSTANCE.getIngredient(ingredientName)) == null) {
						System.out.println(String.format("Fatal: The ingredient '%s' is not part of the Knowledge Base.", ingredientName));
						this.loaded = false;
					}
					else {
						// Copy to keep the knowledge base reference clean
						double quantity = (double) jsonIngredient.get("quantity");
						ingredients.add(new HasIngredient(quantity, ingredient));
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
	
	public boolean isLoaded() {
		return loaded;
	}
	
	// Sums
	public double getGlycemicLoad() {
		double sum = 0;
		for(HasIngredient i : ingredients) {
			sum += i.getGlycemicLoad();
		}
		return (sum / servings);
	}
	public double getCarbs() {
		double sum = 0;
		for(HasIngredient i : ingredients) {
			sum += i.getFractionCarbs();
		}
		return (sum / servings);
	}
	public double getFats() {
		double sum = 0;
		for(HasIngredient i : ingredients) {
			sum += i.getFractionFats();
		}
		return (sum / servings);
	}
	public double getProteins() {
		double sum = 0;
		for(HasIngredient i : ingredients) {
			sum += i.getFractionProteins();
		}
		return (sum / servings);
	}
	public double getFibers() {
		double sum = 0;
		for(HasIngredient i : ingredients) {
			sum += i.getFractionFibers();
		}
		return (sum / servings);
	}
	public double getCalories() {
		double sum = 0;
		for(HasIngredient i : ingredients) {
			sum += i.getCalories();
		}
		return (sum / servings);
	}
	
	// Checks
	public int checkGlycemicLoad() {
		return course.checkGlycemicLoad(getGlycemicLoad());
	}
	public int checkCarbs() {
		return course.checkCarbs(getCarbs());
	}
	public int checkFats() {
		return course.checkFats(getFats());
	}
	public int checkProteins() {
		return course.checkProteins(getProteins());
	}
	public int checkFibers() {
		return course.checkFibers(getFibers());
	}
	public int checkCalories() {
		return course.checkCalories(getCalories());
	}
	
	@Override
	public String toString() {
		return String.format("Recipe: %s for %d (%s)", name, servings, course);
	}
}

