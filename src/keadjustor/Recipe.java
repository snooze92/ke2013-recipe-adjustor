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
import static ch.lambdaj.Lambda.*;

public class Recipe {
	private String name;
	private int servings;
	private Course course;
	
	private boolean loaded;
	
	private List<HasIngredient> ingredients;
	
	// Copy constructor
	public Recipe(Recipe original) {
		this.name = original.name;
		this.servings = original.servings;
		this.course = original.course;
		this.loaded = original.loaded;
		
		this.ingredients = new ArrayList<HasIngredient>();
		for (HasIngredient ingredient : original.ingredients) {
			this.ingredients.add(new HasIngredient(this, ingredient.getQuantity(), ingredient.getIngredient()));
		}
	}
	
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
						ingredients.add(new HasIngredient(this, quantity, ingredient));
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
	
	// Getters
	public boolean isLoaded() {
		return loaded;
	}
	public List<HasIngredient> getIngredients() {
		return ingredients;
	}
	
	// Sums
	public double getGlycemicLoad() {
		return sum(ingredients, on(HasIngredient.class).getGlycemicLoad());		
	}
	public double getFats() {
		return sum(ingredients, on(HasIngredient.class).getFats());
	}
	public double getProteins() {
		return sum(ingredients, on(HasIngredient.class).getProteins());
	}
	public double getFibers() {
		return sum(ingredients, on(HasIngredient.class).getFibers());
	}
	public double getCalories() {
		return sum(ingredients, on(HasIngredient.class).getCalories());
	}
	
	public double evaluate() {
		return evaluate(false);
	}
	public double evaluate(boolean print) {
		double glError = Math.abs(course.checkGlycemicLoad(getGlycemicLoad()));
		double fatsError = Math.abs(course.checkFats(getFats()));
		double proteinsError = Math.abs(course.checkProteins(getProteins()));
		double fibersError = Math.abs(course.checkFibers(getFibers()));
		double caloriesError = Math.abs(course.checkCalories(getCalories()));
		// TODO: Have static final at the top, with 5 smart weights
		double error = glError + fatsError + proteinsError + fibersError + 0.1 * caloriesError;
		
		if (print) {
			System.out.println(String.format("[LOG] Errors=%.2f (GL=%.2f; fats=%.2f; proteins=%.2f; fibers=%.2f; calories=%.2f)",
					error, glError, fatsError, proteinsError, fibersError, caloriesError));
		}
		return (glError == 0 && error < 20) ? 0 : error;
	}
	
	// Checks
	public double checkGlycemicLoad() {
		return course.checkGlycemicLoad(getGlycemicLoad());
	}
	public double checkFats() {
		return course.checkFats(getFats());
	}
	public double checkProteins() {
		return course.checkProteins(getProteins());
	}
	public double checkFibers() {
		return course.checkFibers(getFibers());
	}
	public double checkCalories() {
		return course.checkCalories(getCalories());
	}	
	public int getServings() {
		return servings;
	}
	
	public List<QuantityAdjustment> getPossibleAdjustments() {
		List<QuantityAdjustment> result = new ArrayList<QuantityAdjustment>();
		for (HasIngredient i : ingredients) {
			result.add(new QuantityAdjustment(i.getIngredient(), 1.0 - QuantityAdjustment.adjustmentFactor));
			result.add(new QuantityAdjustment(i.getIngredient(), 1.0 + QuantityAdjustment.adjustmentFactor));
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Recipe: %s for %d (%s)\n", name, servings, course));
		for(HasIngredient hi : getIngredients()) {
			sb.append(hi + "\n");
		}
		return sb.toString();
	}
}

