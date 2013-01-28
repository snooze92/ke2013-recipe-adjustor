package keadjustor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import keadjustor.constraints.MaximumCalories;
import keadjustor.constraints.MaximumGlycemicLoad;
import keadjustor.constraints.MaximumProtein;
import keadjustor.constraints.MaximumSaturatedFat;
import keadjustor.constraints.MinimumCalories;
import keadjustor.constraints.MinimumFiber;
import keadjustor.constraints.MinimumProtein;
import keadjustor.constraints.RecipeConstraint;

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
		double fats = sum(ingredients, on(HasIngredient.class).getFats());
		return fats;
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
	
	public RecipeConstraint verify() {
		for(RecipeConstraint constraint : course.getConstraints())
		{
			
			
			if(constraint instanceof MinimumProtein)
				((MinimumProtein) constraint).verify(getProteins());
			if(constraint instanceof MaximumProtein)
				((MaximumProtein) constraint).verify(getProteins());
			
			if(constraint instanceof MinimumFiber)
				((MinimumFiber) constraint).verify(getFibers());
			
			if(constraint instanceof MaximumSaturatedFat)
				((MaximumSaturatedFat) constraint).verify(getFats());
			
			if(constraint instanceof MinimumCalories)
				((MinimumCalories) constraint).verify(getCalories());
			if(constraint instanceof MaximumCalories)
				((MaximumCalories) constraint).verify(getCalories());
			
			if(constraint instanceof MaximumGlycemicLoad)
				((MaximumGlycemicLoad) constraint).verify(getGlycemicLoad());
							
			if(constraint.isViolated())
				return constraint;
		}
		return null;
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

