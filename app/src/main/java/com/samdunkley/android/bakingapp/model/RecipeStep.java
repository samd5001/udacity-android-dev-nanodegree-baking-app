package com.samdunkley.android.bakingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeStep {

    private String id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

}
