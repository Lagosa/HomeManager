package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Dish;
import com.lagosa.HomeManager.model.Ingredient;
import com.lagosa.HomeManager.model.Visibility;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DishDao {
    int insertDish(Dish newDish);
    void addIngredientsToDish(List<Ingredient> ingredients, int dishId);
    List<Dish> getDishesOfFamily(UUID familyId);
    Dish getDishById(int dishId);
    int insertNewIngredient(Ingredient ingredient);
    Ingredient getIngredient(String ingredientName);
    List<Ingredient> getIngredientsOfADish(int dishId);
    void updateVisibility(int dishId, Visibility visibility);
    int getTypeId(String type);
    List<String> getTypes();
    void increaseNrTimesMade(int dishId);
    List<Ingredient> getAllIngredients();

    void planDish(UUID familyId, int dishId, Date dayWhenPrepare);
    List<Map<String,Object>> getPlannedDishes(UUID familyId, Date startDate, Date endDate);
}
