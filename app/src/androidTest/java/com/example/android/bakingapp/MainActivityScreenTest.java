package com.example.android.bakingapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.example.android.bakingapp.Utilities.Recipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by kanchan on 02-03-2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecyclerViewItem_opensRecipeActivity(){

        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1,"Nutella Pie","",8,"false"));
        recipes.add(new Recipe(2,"Brownies","",8,"false"));
        recipes.add(new Recipe(3,"Yellow Cake","",8,"false"));
        recipes.add(new Recipe(4,"Cheesecake","",8,"false"));

        RecyclerViewInteraction.<Recipe>onRecyclerView(withId(R.id.rv_display_recipes))
                .withItems(recipes)
                .check(new RecyclerViewInteraction.ItemViewAssertion<Recipe>() {
                    @Override
                    public void check(Recipe item, View view, NoMatchingViewException e) {
                        matches(hasDescendant(withText(item.getRecipeName())))
                                .check(view, e);
                    }
                });

    }

}
