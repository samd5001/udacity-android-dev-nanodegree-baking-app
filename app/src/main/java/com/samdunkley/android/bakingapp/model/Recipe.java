package com.samdunkley.android.bakingapp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    private Integer id;
    private Integer servings;
    private String image;
    private List<RecipeIngredient> ingredients;
    private List<RecipeStep> steps;

}
