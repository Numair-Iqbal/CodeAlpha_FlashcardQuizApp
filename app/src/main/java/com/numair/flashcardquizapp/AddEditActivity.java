package com.numair.flashcardquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    EditText etQuestion, etAnswer;
    Button btnSave, btnDelete;
    TextView tvTitle;
    DatabaseHelper db;
    int cardId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        etQuestion = findViewById(R.id.etQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        db = new DatabaseHelper(this);

        Intent intent = getIntent();

        // Agar edit ho raha hai
        if (intent.hasExtra("id")) {
            cardId = intent.getIntExtra("id", -1);
            // Purana data fields mein bhar do
            etQuestion.setText(intent.getStringExtra("question"));
            etAnswer.setText(intent.getStringExtra("answer"));
            btnSave.setText("Update Card");
            btnDelete.setVisibility(android.view.View.VISIBLE);
        } else {
            // Naya card ban raha hai
            btnSave.setText("Save Card");
            btnDelete.setVisibility(android.view.View.GONE);
        }

        // Save ya Update button
        btnSave.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            String answer = etAnswer.getText().toString().trim();

            if (question.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this, "Please enter both Question and Answer!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cardId == -1) {
                db.addCard(question, answer);
                Toast.makeText(this, "Card added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                db.updateCard(cardId, question, answer);
                Toast.makeText(this, "Card updated successfully!", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        // Delete button
        btnDelete.setOnClickListener(v -> {
            db.deleteCard(cardId);
            Toast.makeText(this, "Card deleted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}