package com.example.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.*;

public class MainActivity extends AppCompatActivity {


    TextView textViewQuestion, textViewPoints;
    RadioGroup radioGroup;
    RadioButton rb1, rb2, rb3, rb4;
    Button buttonNext,signout;

    String[] questions = {
            "What is the capital of France?",
            "Which planet is known as the Red Planet?",
            "What is 2 + 2?",
            "Who wrote 'Romeo and Juliet'?",
            "What is the boiling point of water?",
            "What color do you get by mixing red and blue?",
            "Which gas do plants use for photosynthesis?",
            "What is the largest mammal?",
            "Which country is famous for pizza?",
            "How many continents are there?",
            "What is the fastest land animal?",
            "How many legs does a spider have?",
            "Which shape has 3 sides?",
            "Which instrument has keys, pedals, and strings?",
            "What is the largest planet in our solar system?",
            "What language is spoken in Brazil?",
            "What is the process of water turning into vapor called?",
            "Which ocean is the largest?",
            "How many hours are there in a day?",
            "What do bees make?"
    };

    String[][] options = {
            {"Paris", "London", "Berlin", "Rome"},
            {"Earth", "Mars", "Venus", "Saturn"},
            {"3", "4", "5", "6"},
            {"Shakespeare", "Hemingway", "Tolkien", "Rowling"},
            {"90°C", "100°C", "80°C", "70°C"},
            {"Purple", "Green", "Orange", "Yellow"},
            {"Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"},
            {"Elephant", "Whale", "Hippopotamus", "Giraffe"},
            {"USA", "France", "Italy", "Mexico"},
            {"5", "6", "7", "7"},
            {"Cheetah", "Lion", "Horse", "Ostrich"},
            {"6", "8", "10", "12"},
            {"Triangle", "Square", "Circle", "Rectangle"},
            {"Piano", "Guitar", "Drums", "Flute"},
            {"Mars", "Earth", "Jupiter", "Saturn"},
            {"Spanish", "Portuguese", "French", "English"},
            {"Evaporation", "Condensation", "Melting", "Freezing"},
            {"Atlantic", "Indian", "Pacific", "Arctic"},
            {"12", "18", "24", "30"},
            {"Milk", "Wax", "Honey", "Water"}
    };

    int[] correctAnswers = {
            0, 1, 1, 0, 1,
            0, 1, 1, 2, 2,
            0, 1, 0, 0, 2,
            1, 0, 2, 2, 2
    };

    int currentQuestion = 0;
    int points = 0;
    List<Integer> questionOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewPoints = findViewById(R.id.textViewScore);
        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
        buttonNext = findViewById(R.id.buttonNext);
        signout=findViewById(R.id.buttonSignOut);
        GetScore();
        questionOrder = new ArrayList<>();

        for (int i = 0; i < questions.length; i++) {
            questionOrder.add(i);
        }
        Collections.shuffle(questionOrder);

        loadQuestion();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCache();
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonNext.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedIndex = -1;
            if (selectedId == rb1.getId()) selectedIndex = 0;
            else if (selectedId == rb2.getId()) selectedIndex = 1;
            else if (selectedId == rb3.getId()) selectedIndex = 2;
            else if (selectedId == rb4.getId()) selectedIndex = 3;

            int realIndex = questionOrder.get(currentQuestion);
            if (selectedIndex == correctAnswers[realIndex]) {


                IncrementScore();

            }

            currentQuestion++;
            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                showFinalScore();
            }
        });
    }

    private void loadQuestion() {
        int index = questionOrder.get(currentQuestion);
        textViewQuestion.setText(questions[index]);
        rb1.setText(options[index][0]);
        rb2.setText(options[index][1]);
        rb3.setText(options[index][2]);
        rb4.setText(options[index][3]);
        radioGroup.clearCheck();
        textViewPoints.setText("Points: " + points);
    }

    private void showFinalScore() {
        textViewQuestion.setText("Quiz finished!\nYour score: " + points + " / " + questions.length);
        radioGroup.setVisibility(View.GONE);
        buttonNext.setVisibility(View.GONE);
        textViewPoints.setVisibility(View.GONE);
    }
    private void IncrementScore()
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];

                field[0] = "username";


                //Creating array for data
                String[] data = new String[1];

                data[0] = getUsernameFromCache();


                PutData putData = new PutData("http://192.168.1.3/LoginRegister/incrementpoints.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {

                        String result = putData.getResult();

                        if (result.equals("Increment Success"))
                        {

                            points++;
                            textViewPoints.setText("Points: " + points);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                        }

                    }
                }
                //End Write and Read data with URL
            }

        });

    }
    private String getUsernameFromCache() {
        return getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("username", "default_username");
    }

    private void GetScore()
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];

                field[0] = "username";


                //Creating array for data
                String[] data = new String[1];

                data[0] = getUsernameFromCache();


                PutData putData = new PutData("http://192.168.1.3/LoginRegister/getpoints.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {


                        String result = putData.getResult().trim();
                        points=Integer.parseInt(result);
                        textViewPoints.setText("Points: " + points);

                    }
                }
                //End Write and Read data with URL
            }

        });

    }
    private void clearCache() {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }


}

