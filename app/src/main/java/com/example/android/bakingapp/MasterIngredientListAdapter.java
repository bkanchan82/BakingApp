package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Utilities.Ingredient;

import java.util.ArrayList;

/**
 * Created by kanchan on 28-02-2018.
 */

public class MasterIngredientListAdapter extends RecyclerView.Adapter<MasterIngredientListAdapter.IngredientHolder>{

    private ArrayList<Ingredient> ingredientArrayList;

    public MasterIngredientListAdapter(){
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.ingredient_view_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutId,parent, false);

        return new IngredientHolder(view);

    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        Ingredient ingredient = ingredientArrayList.get(position);
        holder.ingregientNameTextView.setText(ingredient.getIngredient());
        holder.measuringUnitTextView.setText(ingredient.getMeasuringUnit());
        holder.quantityTextView.setText(String.valueOf(ingredient.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if(ingredientArrayList !=null) {
            return ingredientArrayList.size();
        }else{
            return 0;
        }
    }

    public void setIngredientArrayList(ArrayList<Ingredient> recipeArrayList){
        this.ingredientArrayList = recipeArrayList;
        notifyDataSetChanged();
    }

    final class IngredientHolder extends RecyclerView.ViewHolder{

        final TextView ingregientNameTextView;
        final TextView measuringUnitTextView;
        final TextView quantityTextView;

        public IngredientHolder(View itemView) {
            super(itemView);
            ingregientNameTextView = itemView.findViewById(R.id.tv_ingredient_name);
            measuringUnitTextView = itemView.findViewById(R.id.tv_measuring_unit);
            quantityTextView = itemView.findViewById(R.id.tv_quantity);
        }
    }

}


