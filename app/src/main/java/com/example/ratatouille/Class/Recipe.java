package com.example.ratatouille.Class;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    String description;
    String id;
    List<String> ingredients;
    String name;
    List<String> tools;
    String foodType;

    public Recipe() {
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public Recipe(String description, String id, List<String> ingredients, String name, List<String> tools, String foodType) {
        this.description = description;
        this.id = id;
        this.ingredients = ingredients;
        this.name = name;
        this.tools = tools;
        this.foodType = foodType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }
}
