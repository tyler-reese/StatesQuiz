package com.example.android.statesquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int quizLength = 10;
    int mode = 0;
    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // get the data passed from MainActivity
        Intent intent = getIntent();
        quizLength = intent.getIntExtra(MainActivity.QUIZ_LENGTH, 10);
        mode = intent.getIntExtra(MainActivity.MODE, 0);

        Spinner spinner = findViewById(R.id.spinnerDifficulty);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(quizLength == 10 ? 0 : quizLength == 25 ? 1 : 2);

        CheckBox modeCheckBox = findViewById(R.id.hardMode);
        modeCheckBox.setChecked(mode == 1);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (first) {
            first = false;
            return;
        }
        String item = parent.getItemAtPosition(pos).toString();
        if (item.equals("10"))
            quizLength = 10;
        else if (item.equals("25"))
            quizLength = 25;
        else if (item.equals("50"))
            quizLength = 50;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Send the option data back to MainActivity.
     */
    public void optionsDone(View view) {
        CheckBox modeCheckBox = findViewById(R.id.hardMode);
        int mode = modeCheckBox.isChecked() ? 1 : 0;
        Intent intent = new Intent();
        intent.putExtra("quizLength", quizLength);
        intent.putExtra("mode", mode);
        setResult(RESULT_OK, intent);
        finish();
    }
}
