package com.example.quizapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizapp.models.Category;
import com.example.quizapp.models.Question;
import com.example.quizapp.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper class manages the SQLite database creation and version management.
 * It also handles all CRUD (Create, Read, Update, Delete) operations for Users, Categories, and Questions.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "QuizApp.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_QUESTIONS = "questions";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_PASSWORD = "password";

    // Category Table Columns
    private static final String KEY_CAT_ID = "id";
    private static final String KEY_CAT_NAME = "name";
    private static final String KEY_CAT_IMAGE = "image_res_id"; // Storing resource ID for simplicity in this project

    // Question Table Columns
    private static final String KEY_QUES_ID = "id";
    private static final String KEY_QUES_TEXT = "question";
    private static final String KEY_QUES_OP1 = "option1";
    private static final String KEY_QUES_OP2 = "option2";
    private static final String KEY_QUES_OP3 = "option3";
    private static final String KEY_QUES_OP4 = "option4";
    private static final String KEY_QUES_ANS = "answer_nr"; // Stores the number of the correct option (1-4)
    private static final String KEY_QUES_CAT_ID = "category_id"; // Foreign key linking to Category table

    private SQLiteDatabase db;

    // Constructor: Initializes the database helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate: Called when the database is created for the first time.
    // We execute SQL queries here to create our tables.
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        // SQL query to create Users table
        final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // SQL query to create Categories table
        final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CAT_NAME + " TEXT,"
                + KEY_CAT_IMAGE + " INTEGER" + ")";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        // SQL query to create Questions table with a foreign key reference to Categories (conceptual)
        final String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + KEY_QUES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_QUES_TEXT + " TEXT,"
                + KEY_QUES_OP1 + " TEXT,"
                + KEY_QUES_OP2 + " TEXT,"
                + KEY_QUES_OP3 + " TEXT,"
                + KEY_QUES_OP4 + " TEXT,"
                + KEY_QUES_ANS + " INTEGER,"
                + KEY_QUES_CAT_ID + " INTEGER" + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        // Populate tables with initial dummy data
        fillCategories();
        fillQuestions();
    }

    // onUpgrade: Called when the database needs to be upgraded (e.g., version change).
    // Here we simply drop the old tables and recreate them.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    // Helper method to add default categories
    private void fillCategories() {
        // Image resource IDs are placeholders (0). In a real app, you would use R.drawable.image_name
        addCategory(new Category("General Knowledge", 0));
        addCategory(new Category("Science", 0));
        addCategory(new Category("History", 0));
    }

    // Insert a category into the database
    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CAT_NAME, category.getName());
        cv.put(KEY_CAT_IMAGE, category.getImageResId());
        db.insert(TABLE_CATEGORIES, null, cv);
    }

    // Helper method to add default questions
    private void fillQuestions() {
        // General Knowledge (Category 1)
        addQuestion(new Question("What is the capital of France?", "Berlin", "Madrid", "Paris", "Rome", 3, 1));
        addQuestion(new Question("Who wrote 'Hamlet'?", "Charles Dickens", "William Shakespeare", "Mark Twain",
                "Leo Tolstoy", 2, 1));
        addQuestion(new Question("Which is the largest animal in the world?", "Blue Whale", "Elephant", "Giraffe", "Shark", 1, 1));
        addQuestion(new Question("Which country is known as the Land of the Rising Sun?", "China", "Japan", "India", "Thailand", 2, 1));

        // Science (Category 2)
        addQuestion(new Question("What is the chemical symbol for Gold?", "Au", "Ag", "Fe", "Pb", 1, 2));
        addQuestion(
                new Question("Which planet is known as the Red Planet?", "Earth", "Jupiter", "Mars", "Venus", 3, 2));
        addQuestion(new Question("What is the hardest natural substance on Earth?", "Gold", "Iron", "Diamond", "Platinum", 3, 2));
        addQuestion(new Question("What is the main gas found in the air we breathe?", "Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen", 2, 2));

        // History (Category 3)
        addQuestion(new Question("Who was the first President of USA?", "Abraham Lincoln", "Thomas Jefferson",
                "George Washington", "John Adams", 3, 3));
        addQuestion(new Question("In which year did WWII end?", "1940", "1945", "1950", "1939", 2, 3));
        addQuestion(new Question("Who was the first man to step on the moon?", "Yuri Gagarin", "Neil Armstrong", "Buzz Aldrin", "Michael Collins", 2, 3));
        addQuestion(new Question("The Great Wall of China was built to keep out whom?", "Mongols", "Romans", "Egyptians", "Greeks", 1, 3));
    }

    // Insert a question into the database
    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_QUES_TEXT, question.getQuestion());
        cv.put(KEY_QUES_OP1, question.getOption1());
        cv.put(KEY_QUES_OP2, question.getOption2());
        cv.put(KEY_QUES_OP3, question.getOption3());
        cv.put(KEY_QUES_OP4, question.getOption4());
        cv.put(KEY_QUES_ANS, question.getAnswerNr());
        cv.put(KEY_QUES_CAT_ID, question.getCategoryId());
        db.insert(TABLE_QUESTIONS, null, cv);
    }

    // ---------------------------------------------------------------------------------------------
    // USER OPERATIONS
    // ---------------------------------------------------------------------------------------------

    // Add a new user (Signup)
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_NAME, user.getUsername());
        cv.put(KEY_USER_PASSWORD, user.getPassword());
        // insert returns the row ID of the newly inserted row, or -1 if an error occurred
        return db.insert(TABLE_USERS, null, cv);
    }

    // Check if a user exists with the given username and password (Login)
    public boolean checkUser(String username, String password) {
        String[] columns = { KEY_USER_ID };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = KEY_USER_NAME + " = ?" + " AND " + KEY_USER_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        
        // Query the database
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        
        // If count > 0, a record exists
        return count > 0;
    }

    // Check if a username is already taken (Signup Validation)
    public boolean checkUserExists(String username) {
        String[] columns = { KEY_USER_ID };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = KEY_USER_NAME + " = ?";
        String[] selectionArgs = { username };
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    // Update password for a specific user
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_PASSWORD, newPassword);
        
        String selection = KEY_USER_NAME + " = ?";
        String[] selectionArgs = { username };
        
        int rowsAffected = db.update(TABLE_USERS, cv, selection, selectionArgs);
        return rowsAffected > 0;
    }

    // ---------------------------------------------------------------------------------------------
    // CATEGORY OPERATIONS
    // ---------------------------------------------------------------------------------------------

    // Get all categories to display in the CategoriesActivity
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CAT_ID)));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CAT_NAME)));
                category.setImageResId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CAT_IMAGE)));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    // ---------------------------------------------------------------------------------------------
    // QUESTION OPERATIONS
    // ---------------------------------------------------------------------------------------------

    // Get questions specific to a category
    public ArrayList<Question> getQuestionsByCategory(int categoryId) {
        ArrayList<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_QUES_CAT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(categoryId) };

        Cursor cursor = db.query(TABLE_QUESTIONS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_QUES_ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUES_TEXT)));
                question.setOption1(cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUES_OP1)));
                question.setOption2(cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUES_OP2)));
                question.setOption3(cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUES_OP3)));
                question.setOption4(cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUES_OP4)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_QUES_ANS)));
                question.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_QUES_CAT_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }
}
