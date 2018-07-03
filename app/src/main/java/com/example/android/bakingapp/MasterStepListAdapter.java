package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Utilities.RecipeStep;

import java.util.ArrayList;

/**
 * Created by kanchan on 28-02-2018.
 */

public class MasterStepListAdapter  extends RecyclerView.Adapter<MasterStepListAdapter.RecipeStepsHolder>{

    private ArrayList<RecipeStep> recipeArrayList;
    private final RecipeStepsAdapterOnItemClickListener recipeStepAdapterOnItemClickListener;

    public MasterStepListAdapter(RecipeStepsAdapterOnItemClickListener recipeStepAdapterOnItemClickListener){
        this.recipeStepAdapterOnItemClickListener = recipeStepAdapterOnItemClickListener;
    }


    public interface RecipeStepsAdapterOnItemClickListener {
        void onRecipeStepItemClickListener(RecipeStep recipeObjecct);
    }

    @Override
    public RecipeStepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.steps_view_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutId,parent, false);

        return new RecipeStepsHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeStepsHolder holder, int position) {

        RecipeStep recipe = recipeArrayList.get(position);
//        Picasso.with(context).load(mRecipe.getImageUrl()).into(holder.posterImageView);
        holder.stepDescriptionTextView.setText(recipe.getShortDescription());

    }

    @Override
    public int getItemCount() {
        if(recipeArrayList !=null) {
            return recipeArrayList.size();
        }else{
            return 0;
        }
    }

    public void setRecipeArrayList(ArrayList<RecipeStep> recipeArrayList){
        this.recipeArrayList = recipeArrayList;
        notifyDataSetChanged();
    }

    final class RecipeStepsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView stepDescriptionTextView;

        public RecipeStepsHolder(View itemView) {
            super(itemView);
            stepDescriptionTextView = itemView.findViewById(R.id.tv_step_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemPosition = getAdapterPosition();
            recipeStepAdapterOnItemClickListener.onRecipeStepItemClickListener(recipeArrayList.get(clickedItemPosition));
        }
    }

}

