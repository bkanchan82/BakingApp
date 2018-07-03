package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.Utilities.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kanchan on 26-02-2018.
 */

public class RecipeBookAdapter  extends RecyclerView.Adapter<RecipeBookAdapter.RecipeBookHolder>{

    private ArrayList<Recipe> recipeArrayList;
    private final RecipeBookAdapterOnItemClickListener recipeBookAdapterOnItemClickListener;
    Context mContext;

    public RecipeBookAdapter(Context context,RecipeBookAdapterOnItemClickListener recipeBookAdapterOnItemClickListener){
        this.recipeBookAdapterOnItemClickListener = recipeBookAdapterOnItemClickListener;
        mContext = context;
    }


    public interface RecipeBookAdapterOnItemClickListener {
        void onRecipeItemClickListener(Recipe recipeObjecct);
    }

    @Override
    public RecipeBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.recipe_card;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutId,parent, false);

        return new RecipeBookHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeBookHolder holder, int position) {

        Recipe recipe = recipeArrayList.get(position);
        if(recipe.getImageUrl()!=null && recipe.getImageUrl().length()>0) {
            Picasso.with(mContext).load(recipe.getImageUrl()).into(holder.posterImageView);
        }
        holder.movieTitleTextView.setText(recipe.getRecipeName());

    }

    @Override
    public int getItemCount() {
        if(recipeArrayList !=null) {
            return recipeArrayList.size();
        }else{
            return 0;
        }
    }

    public void setRecipeArrayList(ArrayList<Recipe> recipeArrayList){
        this.recipeArrayList = recipeArrayList;
        notifyDataSetChanged();
    }

    final class RecipeBookHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView posterImageView;
        final TextView movieTitleTextView;

        public RecipeBookHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.iv_display_recipe);
            movieTitleTextView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemPosition = getAdapterPosition();
            recipeBookAdapterOnItemClickListener.onRecipeItemClickListener(recipeArrayList.get(clickedItemPosition));
        }
    }

}

