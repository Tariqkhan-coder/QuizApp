package com.example.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.db.DatabaseHelper;
import com.example.quizapp.models.Question;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * QuizActivity handles the main quiz logic.
 * It displays questions, handles the timer, checks answers, and calculates the score.
 */
public class QuizActivity extends AppCompatActivity {

    // UI Components
    private TextView tvQuestion, tvScore, tvQuestionCount, tvTimer;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button btnConfirmNext;

    // Quiz Data
    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;

    // Timer Variables
    private ColorStateList textColorDefaultRb;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private static final long COUNTDOWN_IN_MILLIS = 30000; // 30 seconds per question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize UI components
        tvQuestion = findViewById(R.id.tv_question);
        tvScore = findViewById(R.id.tv_score);
        tvQuestionCount = findViewById(R.id.tv_question_count);
        tvTimer = findViewById(R.id.tv_timer);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.rb_option1);
        rb2 = findViewById(R.id.rb_option2);
        rb3 = findViewById(R.id.rb_option3);
        rb4 = findViewById(R.id.rb_option4);
        btnConfirmNext = findViewById(R.id.btn_confirm_next);

        // Save default text color of radio buttons to restore later
        textColorDefaultRb = rb1.getTextColors();

        // Get Category ID passed from CategoriesActivity
        int categoryId = getIntent().getIntExtra("CATEGORY_ID", 0);

        // Initialize Database Helper and fetch questions for the selected category
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionList = dbHelper.getQuestionsByCategory(categoryId);
        questionCountTotal = questionList.size();

        // Check if questions exist
        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found for this category.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Shuffle questions to make the quiz random
        Collections.shuffle(questionList);

        // Start the quiz
        showNextQuestion();

        // Handle Next/Confirm Button logic
        btnConfirmNext.setOnClickListener(v -> {
            if (!answered) {
                // If answer is not locked in, check if an option is selected
                if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                    checkAnswer();
                } else {
                    Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            } else {
                // If already answered, move to next question
                showNextQuestion();
            }
        });
    }

    // Display the next question
    private void showNextQuestion() {
        // Reset radio buttons color and selection
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            // Get current question from list
            currentQuestion = questionList.get(questionCounter);

            // Set text for question and options
            tvQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            tvQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            btnConfirmNext.setText("Confirm"); // Button says "Confirm" until answer is checked

            // Reset and start timer
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

        } else {
            // No more questions, finish quiz
            finishQuiz();
        }
    }

    // Timer Logic
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer(); // Auto-submit when time runs out
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);

        // Change timer color to RED if less than 10 seconds remain
        if (timeLeftInMillis < 10000) {
            tvTimer.setTextColor(Color.RED);
        } else {
            tvTimer.setTextColor(Color.BLACK);
        }
    }

    // Check selected answer
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel(); // Stop timer

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        // Calculate index of selected child (0-3), add 1 to match answerNr (1-4)
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            tvScore.setText("Score: " + score);
        }

        showSolution();
    }

    // Highlight the correct answer
    private void showSolution() {
        // Set all to red initially (indicating incorrect)
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        // Set the correct one to Green
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 3 is correct");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 4 is correct");
                break;
        }

        // Change button text based on whether it's the last question
        if (questionCounter < questionCountTotal) {
            btnConfirmNext.setText("Next");
        } else {
            btnConfirmNext.setText("Finish");
        }
    }

    // Finish Quiz and navigate to Result Activity
    private void finishQuiz() {
        Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
        resultIntent.putExtra("SCORE", score);
        resultIntent.putExtra("TOTAL_QUESTIONS", questionCountTotal);
        startActivity(resultIntent);
        finish();
    }

    // Prevent memory leaks by cancelling timer if activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
