package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Dish;
import com.lagosa.HomeManager.model.Ingredient;
import com.lagosa.HomeManager.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class DishDaoImpl implements DishDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DishDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertDish(Dish newDish) {
        String sql = "INSERT INTO dishes (familyid,submitter,name,type,recipe,visibility) VALUES (?,?,?,?,?,?)";
        int rows = jdbcTemplate.update(sql,newDish.getFamilyId(),newDish.getSubmitter(),newDish.getName(),newDish.getType(),newDish.getRecipe(),newDish.getVisibility().toString());

        int idLast = 0;
        if(rows == 1){
            sql = "SELECT MAX(id) FROM dishes";
            idLast = jdbcTemplate.queryForObject(sql, Integer.class);
        }
        return idLast;
    }

    @Override
    public void addIngredientsToDish(List<Ingredient> ingredients, int dishId) {
        String sql = "INSERT INTO dish_ingredients (dish,ingredient,quantity) VALUES (?,?,?)";

        for(Ingredient ingredient : ingredients){
            jdbcTemplate.update(sql,dishId,ingredient.getId(),ingredient.getQuantity());
        }
    }

    @Override
    public List<Dish> getDishesOfFamily(UUID familyId) {
        String sql = "SELECT dishes.id,dishes.familyid,dishes.submitter,dishes.name,dishes.type,dishes.recipe,dishes.visibility,dishes.nrtimesmade," +
                "dishtypes.type AS typeName,users.nickname AS submitterName FROM dishes INNER JOIN dishtypes ON dishtypes.id = dishes.type " +
                "INNER JOIN users ON users.id = dishes.submitter WHERE dishes.familyid = ? ORDER BY dishes.type ASC, dishes.nrtimesmade DESC";

        List<Dish> dishList = jdbcTemplate.query(sql, new DishMapper(),familyId);

        sql = "SELECT di.id,di.dish,di.ingredient,di.quantity," +
                "ingredients.ingredient AS ingredientName, ingredients.measurementunit FROM dish_ingredients AS di " +
                "INNER JOIN ingredients ON ingredients.id = di.ingredient WHERE di.dish = ? ORDER BY ingredientName ASC";
        for(Dish dish : dishList){

            dish.setIngredients(jdbcTemplate.query(sql,new IngredientMapper(),dish.getId()));
        }
        return dishList;
    }

    @Override
    public Dish getDishById(int dishId) {
        String sql = "SELECT dishes.id,dishes.family,dishes.submitter,dishes.name,dishes.type,dishes.recipe,dishes.visibility,dishes.nrtimesmade," +
                " users.nickname AS submitterName, dishtypes.type AS typeName FROM dishes INNER JOIN users ON users.id = dishes.submitterid" +
                " INNER JOIN dishtypes ON dishtypes.id = dishes.type WHERE dishes.id = ?";
        return jdbcTemplate.queryForObject(sql,new DishMapper(),dishId);
    }

    @Override
    public int insertNewIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredients (ingredient,measurementunit) VALUES (?,?)";
        int rowsUpdated = jdbcTemplate.update(sql,ingredient.getName(),ingredient.getMeasurementUnit());

        int ingredientId = 0;
        if(rowsUpdated == 1){
            sql = "SELECT MAX(id) FROM ingredients";
            ingredientId = jdbcTemplate.queryForObject(sql,Integer.class);
        }
        return ingredientId;
    }

    @Override
    public Ingredient getIngredient(String ingredientName){
        String sql = "SELECT id AS ingredient,ingredient AS ingredientName,measurementunit, -1 AS quantity FROM ingredients WHERE ingredients.ingredient = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new IngredientMapper(), ingredientName);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Ingredient> getIngredientsOfADish(int dishId) {
        String sql = "SELECT ingrd.ingredient,ingr.ingredient AS ingredientName, ingr.measurementunit, ingrd.quantity FROM dish_ingredients AS ingrd " +
                "INNER JOIN ingredients AS ingr ON ingr.id = ingrd.ingredient WHERE ingrd.dish = ?";
        return jdbcTemplate.query(sql,new IngredientMapper(),dishId);
    }

    @Override
    public void updateVisibility(int dishId, Visibility visibility) {
        String sql = "UPDATE dishes SET visibility = ? WHERE id = ?";
        jdbcTemplate.update(sql,visibility.toString(),dishId);
    }

    @Override
    public int getTypeId(String type) {
        String sql = "SELECT id FROM dishtypes WHERE type = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,type);
    }

    @Override
    public List<String> getTypes() {
        String sql = "SELECT type FROM dishtypes";
        return jdbcTemplate.query(sql, (rs,rownum) -> {
            return rs.getString("type");
        });
    }

    @Override
    public void increaseNrTimesMade(int dishId) {
        String sql = "UPDATE dishes SET nrtimesmade = (SELECT nrtimesmade FROM dishes WHERE id = ?) + 1 WHERE id = ?";
        jdbcTemplate.update(sql,dishId,dishId);
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        String sql = "SELECT id AS ingredient,ingredient AS ingredientName,measurementunit, -1 AS quantity FROM ingredients ORDER BY ingredientName ASC";
        return jdbcTemplate.query(sql,new IngredientMapper());
    }

    @Override
    public void planDish(UUID familyId, int dishId, Date dayWhenPrepare) {
        String sql = "INSERT INTO dishPlans (family,dish,day) VALUES (?,?,?)";
        jdbcTemplate.update(sql,familyId,dishId,dayWhenPrepare);
    }

    @Override
    public List<Map<String,Object>> getPlannedDishes(UUID familyId, Date startDate, Date endDate) {
        String sql = "SELECT dp.day,dp.dish AS id,d.name,d.type,t.type AS typeName " +
                "FROM dishPlans AS dp INNER JOIN dishes AS d ON dp.dish = d.id INNER JOIN dishTypes AS t ON t.id = d.type " +
                "WHERE dp.family = ? AND (dp.day >= ? AND dp.day <= ?)";
        return jdbcTemplate.query(sql, (rs,rowNum) -> {
            Map<String,Object> map = new HashMap<>();

            map.put("day", rs.getDate("day"));
            map.put("dishName",rs.getString("name"));
            map.put("id",rs.getInt("id"));
            map.put("type",rs.getInt("type"));
            map.put("typeName",rs.getString("typeName"));

            return map;
        },familyId,startDate,endDate);
    }

    private static final class DishMapper implements RowMapper<Dish>{

        @Override
        public Dish mapRow(ResultSet rs, int rowNum) throws SQLException {
            Dish dish = new Dish((UUID) rs.getObject("familyid"),
                    (UUID) rs.getObject("submitter"), rs.getString("name"),
                    rs.getInt("type"),rs.getString("recipe"),Visibility.valueOf(rs.getString("visibility")));
            dish.setSubmitterName(rs.getString("submitterName"));
            dish.setTypeName(rs.getString("typeName"));
            dish.setId(rs.getInt("id"));
            dish.setNrTimesMade(rs.getInt("nrtimesmade"));

            return dish;
        }
    }

    private static final class IngredientMapper implements RowMapper<Ingredient>{

        @Override
        public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ingredient ingredient = new Ingredient(rs.getString("ingredientName"),
                    rs.getString("measurementunit"));
            ingredient.setQuantity(rs.getString("quantity"));
            ingredient.setId(rs.getInt("ingredient"));
            return ingredient;
        }
    }
}
