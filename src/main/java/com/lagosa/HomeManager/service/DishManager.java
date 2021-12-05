package com.lagosa.HomeManager.service;

import com.lagosa.HomeManager.dao.DishDao;
import com.lagosa.HomeManager.model.Dish;
import com.lagosa.HomeManager.model.Ingredient;
import com.lagosa.HomeManager.model.User;
import com.lagosa.HomeManager.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DishManager {

    private final DishDao dishDao;
    private final FamilyManager familyManager;

    @Autowired
    public DishManager(DishDao dishDao, FamilyManager familyManager){
        this.dishDao = dishDao;
        this.familyManager = familyManager;
    }

    public void insertDish(UUID userId, String name, String type, String recipe, Visibility visibility, List<Ingredient> ingredientsList){
        // ToDo: Add feature to insert images with dishes

        User user = familyManager.getUser(userId);

        int typeId = dishDao.getTypeId(type);
        int dishId = dishDao.insertDish(new Dish(user.getFamilyId(),user.getId(),name,typeId,recipe,visibility));

        int newIngredientId;
        for(Ingredient ingredient : ingredientsList){
            Ingredient ingredientFromDB = dishDao.getIngredient(ingredient.getName());

            if(ingredientFromDB == null){
                newIngredientId = dishDao.insertNewIngredient(ingredient);
            }else{
                newIngredientId = ingredientFromDB.getId();
            }
            ingredient.setId(newIngredientId);
        }

        dishDao.addIngredientsToDish(ingredientsList,dishId);
    }

    public List<Dish> getDishesOfAFamily(UUID userId){
        User user = familyManager.getUser(userId);

        return dishDao.getDishesOfFamily(user.getFamilyId());
    }

    public List<String> getTypes(){
        return dishDao.getTypes();
    }

    public void increaseNrTimesMade(int dishId){
        dishDao.increaseNrTimesMade(dishId);
    }

    public void changeVisibility(int dishId,Visibility visibility){
        dishDao.updateVisibility(dishId,visibility);
    }

    public List<Ingredient> getAllIngredients(){
        return dishDao.getAllIngredients();
    }
}
