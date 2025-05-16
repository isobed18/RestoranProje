package org.restoranproje.model;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

    private String name;
    private String description;
    private int price;
    private MenuItemType type;

    private ArrayList<StockItem> items;
    private List<RecipeIngredient> recipe = new ArrayList<>();
    private FoodStatus foodStatus = FoodStatus.AVAILABLE; // varsayılan açık

    public MenuItem(String name, String description, MenuItemType type, int price, ArrayList<StockItem> items) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.items = items;
    }

    public MenuItem(String name, String description, MenuItemType type, int price) {
    this.name = name;
    this.description = description;
    this.type = type;
    this.price = price;
    this.items = new ArrayList<>(); 
}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public ArrayList<StockItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<StockItem> items) {
        this.items = items;
    }

    public List<RecipeIngredient> getRecipe() {
        return recipe;
    }

    public void setRecipe(List<RecipeIngredient> recipe) {
        this.recipe = recipe;
    }

    public FoodStatus getFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(FoodStatus foodStatus) {
        this.foodStatus = foodStatus;
    }

}
