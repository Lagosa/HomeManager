package com.lagosa.HomeManager.service;

import com.lagosa.HomeManager.dao.DishDao;
import com.lagosa.HomeManager.exceptions.ApiRequestException;
import com.lagosa.HomeManager.model.Dish;
import com.lagosa.HomeManager.model.Ingredient;
import com.lagosa.HomeManager.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.logging.Logger;

@Service
public class DishManager {

    private final DishDao dishDao;
    private final FamilyManager familyManager;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    public DishManager(DishDao dishDao, FamilyManager familyManager){
        this.dishDao = dishDao;
        this.familyManager = familyManager;
    }

    public void insertDish(UUID userId, String name, String type, String recipe, Visibility visibility, List<Ingredient> ingredientsList){
        // ToDo: Add feature to insert images with dishes

        int typeId = dishDao.getTypeId(type);
        int dishId = dishDao.insertDish(new Dish(familyManager.getFamilyOfUser(userId),userId,name,typeId,recipe,visibility));

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
        return dishDao.getDishesOfFamily(familyManager.getFamilyOfUser(userId));
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

    public Dish getRandomDish(UUID userId,int typeId){
        List<Dish> dishes = getDishesOfAFamily(userId);
        List<Dish> dishesOfType = new ArrayList<>();

        for(Dish dish : dishes){
            if(dish.getType() == typeId){
                dishesOfType.add(dish);
            }
        }

        int sizeOfList = dishesOfType.size();
        if(sizeOfList == 0){
            throw new ApiRequestException("No dish of such type found!");
        }

        long randomNr = Math.round(Math.random() * (sizeOfList - 1) + 1);

        return dishesOfType.get(Math.toIntExact(randomNr-1));
    }

    public void planDish(UUID userId, int dishId, Date dayWhenPrepare){
        dishDao.planDish(familyManager.getFamilyOfUser(userId),dishId,dayWhenPrepare);
    }

    public List<Map<String,Object>> getPlannedDishes(UUID userId, Date startDate, Date endDate){
       return dishDao.getPlannedDishes(familyManager.getFamilyOfUser(userId),startDate,endDate);
    }

    public List<Map<String,Object>> getListOfIngredientsForADay(UUID userId, Date day){
        List<Map<String,Object>> dishes = dishDao.getPlannedDishes(familyManager.getFamilyOfUser(userId),day,day);

        List<Map<String,Object>> ingredientList = new ArrayList<>();
        for(Map<String,Object> dish : dishes){
            Map<String,Object> newMap = new HashMap<>();
            newMap.put("dishName",dish.get("dishName"));
            newMap.put("ingredients",dishDao.getIngredientsOfADish((Integer) dish.get("id")));

            ingredientList.add(newMap);
        }

        return ingredientList;
    }
}
