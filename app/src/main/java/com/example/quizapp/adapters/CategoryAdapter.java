package com.example.quizapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.QuizActivity;
import com.example.quizapp.R;
import com.example.quizapp.models.Category;

import java.util.List;

/**
 * CategoryAdapter binds the category data to the RecyclerView.
 * It handles the creation of view holders and binding of data to individual items.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    // Constructor to pass context and data list
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Get the category object at the current position
        Category category = categoryList.get(position);
        
        // Set the data to the views
        holder.tvName.setText(category.getName());

        // Handle Item Click -> Start Quiz for this category
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("CATEGORY_ID", category.getId()); // Pass category ID to QuizActivity
            context.startActivity(intent);
        });
    }

    // Returns the total number of items in the data set held by the adapter
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // Inner class for the ViewHolder
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the item_category.xml layout
            tvName = itemView.findViewById(R.id.tv_category_name);
        }
    }
}
