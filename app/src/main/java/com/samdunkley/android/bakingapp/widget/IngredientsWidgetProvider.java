package com.samdunkley.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.samdunkley.android.bakingapp.BuildConfig;
import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.view.activities.MainActivity;
import com.samdunkley.android.bakingapp.view.activities.RecipeDetailsListActivity;

import static android.content.Context.MODE_PRIVATE;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        SharedPreferences prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        String recipeName = prefs.getString(RecipeDetailsListActivity.PREF_NAME, context.getString(R.string.widget_placeholder));
        String recipeIngredients = prefs.getString(RecipeDetailsListActivity.PREF_INGREDIENTS, null);

        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.setTextViewText(R.id.widget_recipe_ingredients, recipeIngredients == null ? "" : recipeIngredients);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

