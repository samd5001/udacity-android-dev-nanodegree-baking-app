package com.samdunkley.android.bakingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredient {

    private Integer quantity;
    private String measure;
    private String ingredient;

}
