package com.example.android.bakingapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kanchan on 02-03-2018.
 */

class RecyclerRecipeStepViewAssertion<RecipeStep> implements ViewAssertion {
    private final int position;
    private final RecipeStep item;
    private final RecyclerStepViewInteraction.ItemViewAssertion<RecipeStep> itemViewAssertion;

    public RecyclerRecipeStepViewAssertion(int position, RecipeStep item, RecyclerStepViewInteraction.ItemViewAssertion<RecipeStep> itemViewAssertion) {
        this.position = position;
        this.item = item;
        this.itemViewAssertion = itemViewAssertion;
    }

    @Override
    public final void check(View view, NoMatchingViewException e) {
        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.ViewHolder viewHolderForPosition = recyclerView.findViewHolderForLayoutPosition(position);
        if (viewHolderForPosition == null) {
            throw (new PerformException.Builder())
                    .withActionDescription(toString())
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(new IllegalStateException("No view holder at position: " + position))
                    .build();
        } else {
            View viewAtPosition = viewHolderForPosition.itemView;
            itemViewAssertion.check(item, viewAtPosition, e);
        }
    }
}
