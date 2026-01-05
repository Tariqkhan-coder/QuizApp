package com.example.quizapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.adapters.CategoryAdapter;
import com.example.quizapp.db.DatabaseHelper;
import com.example.quizapp.models.Category;

import java.util.List;

/**
 * CategoriesActivity displays the list of available quiz categories.
 * It uses a RecyclerView to show the list efficiently.
 */
public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // Initialize RecyclerView
        rvCategories = findViewById(R.id.rv_categories);
        // Set LayoutManager (Linear default for vertical list)
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);
        
        // Fetch all categories from the database
        List<Category> categoryList = databaseHelper.getAllCategories();

        // Initialize Adapter with the category list and set it to RecyclerView
        categoryAdapter = new CategoryAdapter(this, categoryList);
        rvCategories.setAdapter(categoryAdapter);
    }
}
