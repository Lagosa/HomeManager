package com.lagosa.HomeManager.model;

import java.util.List;
import java.util.UUID;

public class Dish {

    public Dish(UUID familyId, UUID submitter, String name, int type, String recipe, Visibility visibility) {
        this.familyId = familyId;
        this.submitter = submitter;
        this.name = name;
        this.type = type;
        this.recipe = recipe;
        this.visibility = visibility;
    }

    public int getId() {
        return id;
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public UUID getSubmitter() {
        return submitter;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRecipe() {
        return recipe;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNrTimesMade() {
        return nrTimesMade;
    }

    public void setNrTimesMade(int nrTimesMade) {
        this.nrTimesMade = nrTimesMade;
    }

    private int id;
    private final UUID familyId;
    private final UUID submitter;
    private String submitterName;
    private final String name;
    private final int type;
    private String typeName;
    private final String recipe;
    private final Visibility visibility;
    private List<Ingredient> ingredients;
    private int nrTimesMade;
}
