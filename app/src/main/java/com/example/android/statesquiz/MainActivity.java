package com.example.android.statesquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    public static final String QUIZ_LENGTH = "com.example.android.statesquiz.QUIZ_LENGTH";
    public static final String MODE = "com.example.android.statesquiz.MODE";
    int quizLength = 10;
    int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startQuiz(View view) {
        Intent intent = new Intent(this, StateQuestionActivity.class);
        intent.putExtra(QUIZ_LENGTH, quizLength);
        intent.putExtra(MODE, mode);
        startActivity(intent);
    }

    public void setOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra(QUIZ_LENGTH, quizLength);
        intent.putExtra(MODE, mode);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                quizLength = data.getIntExtra("quizLength", 10);
                mode = data.getIntExtra("mode", 10);
            }
        }
    }
}
