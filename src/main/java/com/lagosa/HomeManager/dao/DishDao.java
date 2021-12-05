package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Dish;
import com.lagosa.HomeManager.model.Ingredient;
import com.lagosa.HomeManager.model.Visibility;

import java.util.List;
import java.util.UUID;

public interface DishDao {
    int insertDish(Dish newDish);
    void addIngredientsToDish(List<Ingredient> ingredients, int dishId);
    List<Dish> getDishesOfFamily(UUID familyId);
    Dish getDishById(int dishId);
    int insertNewIngredient(Ingredient ingredient);
    Ingredient getIngredient(String ingredientName);
    Ingredient getIngredientsOfADish(int dishId);
    void updateVisibility(int dishId, Visibility visibility);
    int getTypeId(String type);
    List<String> getTypes();
    void increaseNrTimesMade(int dishId);
    List<Ingredient> getAllIngredients();
}
