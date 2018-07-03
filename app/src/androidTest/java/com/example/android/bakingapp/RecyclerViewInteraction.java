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

public class RecyclerViewInteraction<Recipe> {

    private final Matcher<View> viewMatcher;
    private List<Recipe> items;

    private RecyclerViewInteraction(Matcher<View> viewMatcher) {
        this.viewMatcher = viewMatcher;
    }

    public static <Recipe> RecyclerViewInteraction<Recipe> onRecyclerView(Matcher<View> viewMatcher) {
        return new RecyclerViewInteraction<>(viewMatcher);
    }

    public RecyclerViewInteraction<Recipe> withItems(List<Recipe> items) {
        this.items = items;
        return this;
    }

    public RecyclerViewInteraction<Recipe> check(ItemViewAssertion<Recipe> itemViewAssertion) {
        for (int i = 0; i < items.size(); i++) {
            Recipe recipe = items.get(i);
            onView(viewMatcher)
                    .perform(scrollToPosition(i))
                    .check(new RecyclerItemViewAssertion<>(i, recipe, itemViewAssertion));
        }
        return this;
    }

    public interface ItemViewAssertion<Recipe> {
        void check(Recipe item, View view, NoMatchingViewException e);
    }

}
