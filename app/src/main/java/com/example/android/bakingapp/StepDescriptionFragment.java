package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Utilities.RecipeStep;

/**
 * Created by kanchan on 28-02-2018.
 */

public class StepDescriptionFragment extends Fragment {

    private static final String TAG = StepDescriptionFragment.class.getSimpleName();
    private static final String DESCRIPTION = "description";

    private RecipeStep recipeStep;

    public void setRecipeStep(RecipeStep recipeStep) {
        this.recipeStep = recipeStep;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_description, container, false);

        TextView stepDescriptionTextView = view.findViewById(R.id.tv_display_step_description);
        if (recipeStep != null) {
            stepDescriptionTextView.setText(recipeStep.getDescription());
        }

        if (savedInstanceState != null) {
            String description = savedInstanceState.getString(DESCRIPTION);
            stepDescriptionTextView.setText(description);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeStep != null)
            outState.putString(DESCRIPTION, recipeStep.getDescription());
    }
}
