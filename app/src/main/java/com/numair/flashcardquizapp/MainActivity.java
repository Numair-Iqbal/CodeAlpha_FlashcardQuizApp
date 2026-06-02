package com.numair.flashcardquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvQuestion, tvAnswer, tvCardCount;
    Button btnShowAnswer, btnNext, btnPrevious, btnAddCard, btnEditCard;
    DatabaseHelper db;
    List<Flashcard> cardList;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvAnswer = findViewById(R.id.tvAnswer);
        tvCardCount = findViewById(R.id.tvCardCount);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnAddCard = findViewById(R.id.btnAddCard);
        btnEditCard = findViewById(R.id.btnEditCard);

        db = new DatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardList = db.getAllCards();
        currentIndex = 0;
        showCard();
    }

    private void showCard() {
        if (cardList.isEmpty()) {
            tvQuestion.setText("No cards found. Press + Add New Card!");
            tvAnswer.setVisibility(View.GONE);
            tvCardCount.setText("0 Cards");
            btnEditCard.setVisibility(View.GONE);
            btnShowAnswer.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
            btnPrevious.setVisibility(View.GONE);
            return;
        }

        btnEditCard.setVisibility(View.VISIBLE);
        btnShowAnswer.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        btnPrevious.setVisibility(View.VISIBLE);

        Flashcard card = cardList.get(currentIndex);
        tvQuestion.setText(card.getQuestion());
        tvAnswer.setText(card.getAnswer());
        tvAnswer.setVisibility(View.GONE);
        tvCardCount.setText("Card " + (currentIndex + 1) + " of " + cardList.size());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Show Answer
        btnShowAnswer.setOnClickListener(v -> {
            if (cardList.isEmpty()) return;
            tvAnswer.setVisibility(View.VISIBLE);
        });

        // Next
        btnNext.setOnClickListener(v -> {
            if (cardList.isEmpty()) return;
            if (currentIndex < cardList.size() - 1) {
                currentIndex++;
                showCard();
            } else {
                Toast.makeText(this, "This is the last card!", Toast.LENGTH_SHORT).show();
            }
        });

        // Previous
        btnPrevious.setOnClickListener(v -> {
            if (cardList.isEmpty()) return;
            if (currentIndex > 0) {
                currentIndex--;
                showCard();
            } else {
                Toast.makeText(this, "This is the first card!", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit / Delete
        btnEditCard.setOnClickListener(v -> {
            if (cardList.isEmpty()) return;
            Flashcard card = cardList.get(currentIndex);
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra("id", card.getId());
            intent.putExtra("question", card.getQuestion());
            intent.putExtra("answer", card.getAnswer());
            startActivity(intent);
        });

        // Add New Card
        btnAddCard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddEditActivity.class));
        });
    }
}