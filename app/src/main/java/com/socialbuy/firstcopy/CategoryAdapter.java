package com.socialbuy.firstcopy;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialbuy.firstcopy.pojo.Categories;

import java.util.ArrayList;

/**
 * Created by kushansingh on 31/10/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private Context context;
    private ArrayList<Categories> categories;

    public CategoryAdapter(Context context, ArrayList<Categories> categories) {

        this.context = context;
        this.categories = categories;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_categories,parent,false);
        CategoryHolder categoryHolder = new CategoryHolder(v);
        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf");
        holder.tvCategoryName.setTypeface(typeface);

        holder.ivCategory.setImageResource(categories.get(position).getCategoryImage());
        holder.tvCategoryName.setText(categories.get(position).getCategoryName());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{

        private ImageView ivCategory;
        private TextView tvCategoryName;

        public CategoryHolder(View itemView) {
            super(itemView);

            ivCategory = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
