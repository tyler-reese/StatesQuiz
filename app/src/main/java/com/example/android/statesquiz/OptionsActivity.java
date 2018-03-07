package com.example.android.statesquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int quizLength = 10;
    private int mode = 0;
    private boolean first = true;
    private CheckBox modeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // get the data passed from MainActivity
        Intent intent = getIntent();
        quizLength = intent.getIntExtra(MainActivity.EXTRA_QUIZ_LENGTH, 10);
        mode = intent.getIntExtra(MainActivity.EXTRA_MODE, 0);

        Spinner spinner = findViewById(R.id.spinnerDifficulty);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(quizLength == 10 ? 0 : quizLength == 25 ? 1 : 2);

        modeCheckBox = findViewById(R.id.hardMode);
        modeCheckBox.setChecked(mode == 1);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (first) {
            first = false;
            return;
        }
        String item = parent.getItemAtPosition(pos).toString();
        switch (item) {
            case "25":
                quizLength = 25;
                break;
            case "50":
                quizLength = 50;
                break;
            default:
                quizLength = 10;
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Send the option data back to MainActivity.
     */
    public void optionsDone(View view) {
        int mode = modeCheckBox.isChecked() ? 1 : 0;
        Intent intent = new Intent();
        intent.putExtra("quizLength", quizLength);
        intent.putExtra("mode", mode);
        setResult(RESULT_OK, intent);
        finish();
    }
}
