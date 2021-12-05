package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.model.Dish;
import com.lagosa.HomeManager.model.Ingredient;
import com.lagosa.HomeManager.model.Visibility;
import com.lagosa.HomeManager.service.DishManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("dish")
public class DishAPI {

    private final DishManager dishManager;
    private final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    public DishAPI(DishManager dishManager){
        this.dishManager = dishManager;
    }

    @PostMapping(path = "/insert/{userId}/{name}/{type}/{visibility}")
    public void insertDish(@PathVariable("userId") UUID userId, @PathVariable("name") String name, @PathVariable("type") String type,
                                         @PathVariable("visibility") String visibility, @RequestBody Map<String, Object> body){
        String recipe = body.get("recipe").toString();

        List<Ingredient> ingredientList = new ArrayList<>();
        for(Object obj: (ArrayList) body.get("ingredients")){
            Map<String,String> map = (Map<String, String>) obj;

            Ingredient ingredient = new Ingredient(map.get("name"),map.get("measurementUnit"));
            ingredient.setQuantity(map.get("quantity"));

            ingredientList.add(ingredient);
        }
        dishManager.insertDish(userId,name,type,recipe, Visibility.valueOf(visibility),ingredientList);
    }

    @GetMapping(path = "/getDishes/{userId}")
    public List<Dish> getDishesOfAFamily(@PathVariable("userId") UUID userId){
        return dishManager.getDishesOfAFamily(userId);
    }

    @GetMapping(path = "/getTypes")
    public List<String> getTypes(){
        return dishManager.getTypes();
    }

    @PostMapping(path = "/dishMade/{dishId}")
    public void increaseNrTimesMade(@PathVariable("dishId") int dishId){
        dishManager.increaseNrTimesMade(dishId);
    }

    @PostMapping(path = "/changeVisibility/{dishId}/{visibility}")
    public void changeVisibility(@PathVariable("dishId") int dishId,@PathVariable("visibility") String visibility){
        dishManager.changeVisibility(dishId,Visibility.valueOf(visibility));
    }

    @GetMapping(path = "/getAllIngredients")
    public List<Ingredient> getAllIngredients(){
        return dishManager.getAllIngredients();
    }

    @GetMapping(path = "/getRandom/{userId}/{typeId}")
    public Dish getRandomDish(@PathVariable("userId") UUID userId,@PathVariable("typeId") int typeId){
        return dishManager.getRandomDish(userId,typeId);
    }

    @PostMapping(path = "/plan/{userId}/{dishId}/{day}")
    public void planDish(@PathVariable("userId") UUID userId,@PathVariable("dishId") int dishId,@PathVariable("day") String dayToPrepare){
        dishManager.planDish(userId,dishId, Date.valueOf(dayToPrepare));
    }

    @GetMapping(path = "/getPlan/{userId}/{start}/{end}")
    public List<Map<String,Object>> getPlannedDishes(@PathVariable("userId") UUID userId,@PathVariable("start") String startDate,@PathVariable("end") String endDate){
        return dishManager.getPlannedDishes(userId,Date.valueOf(startDate),Date.valueOf(endDate));
    }
}
