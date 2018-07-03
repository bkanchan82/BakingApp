package com.example.android.bakingapp;

import android.support.test.espresso.NoMatchingViewException;
import android.view.View;

import org.hamcrest.Matcher;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;

/**
 * Created by kanchan on 02-03-2018.
 */

public class RecyclerStepViewInteraction<RecipeStep> {

    private final Matcher<View> viewMatcher;
    private List<RecipeStep> items;

    private RecyclerStepViewInteraction(Matcher<View> viewMatcher) {
        this.viewMatcher = viewMatcher;
    }

    public static <RecipeStep> RecyclerStepViewInteraction<RecipeStep> onRecyclerView(Matcher<View> viewMatcher) {
        return new RecyclerStepViewInteraction<>(viewMatcher);
    }

    public RecyclerStepViewInteraction<RecipeStep> withItems(List<RecipeStep> items) {
        this.items = items;
        return this;
    }

    public RecyclerStepViewInteraction<RecipeStep> check(ItemViewAssertion<RecipeStep> itemViewAssertion) {
        for (int i = 1; i < items.size(); i++) {
            RecipeStep recipe = items.get(i);
            onView(viewMatcher)
                    .perform(scrollToPosition(i))
                    .check(new RecyclerRecipeStepViewAssertion<>(i, recipe, itemViewAssertion));
        }
        return this;
    }

    public interface ItemViewAssertion<RecipeStep> {
        void check(RecipeStep item, View view, NoMatchingViewException e);
    }

}

