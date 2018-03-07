package com.example.android.statesquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StateQuestionActivity extends AppCompatActivity {
    // Logging tag
    private final String TAG = "StateQuestionActivity";

    // keys used for savedInstanceState
    private final String QUIZ_LENGTH_KEY = "quizLength";
    private final String MODE = "mode";
    private final String SCORE = "score";
    private final String POS = "pos";
    private final String STATES = "states";
    private final String ANSWERS = "answers";
    private final String ANSWER = "answer";
    private final String FINISHED = "finished";
    private final String IMG_RESOURCE = "imgResource";

    // quiz data
    private int quizLength = 10;
    private int mode = 0;
    private int score = 0;
    private int pos = 0;
    private int imgResource = 0;
    private String answer = "";
    private boolean finished = false;
    private String[] states = {
            "alabama", "alaska", "arkansas", "arizona", "california",
            "colorado", "connecticut", "delaware", "florida", "georgia",
            "hawaii", "idaho", "illinois", "indiana", "iowa",
            "kansas", "kentucky", "louisiana", "maine", "maryland",
            "massachussets", "michigan", "minnesota", "mississippi", "missouri",
            "montana", "nebraska", "nevada", "new hampshire", "new jersey",
            "new mexico", "new york", "north carolina", "north dakota", "ohio",
            "oklahoma", "oregon", "pennsylvania", "rhode island", "south carolina",
            "south dakota", "tennessee", "texas", "utah", "vermont",
            "virginia", "washington", "west virginia", "wisconsin", "wyoming"
    };
    private String[] answers;

    // UI elements
    private LinearLayout fillInTheBlank;
    private LinearLayout multipleChoice;
    private ImageView stateImageView;
    private RadioGroup stateRadioGroup1;
    private RadioGroup stateRadioGroup2;
    private RadioButton stateAnswer1;
    private RadioButton stateAnswer2;
    private RadioButton stateAnswer3;
    private RadioButton stateAnswer4;
    private TextView scoreTextView;
    private TextView prompt;
    private TextView questionNumber;
    private TextView finalScore;
    private EditText textAnswer;
    private Button resetButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_question);

        fillInTheBlank = findViewById(R.id.fillInTheBlank);
        multipleChoice = findViewById(R.id.multipleChoice);
        stateImageView = findViewById(R.id.stateImage);
        stateRadioGroup1 = findViewById(R.id.stateRadio1);
        stateRadioGroup1.setOnCheckedChangeListener(changeGroup1);
        stateRadioGroup2 = findViewById(R.id.stateRadio2);
        stateRadioGroup2.setOnCheckedChangeListener(changeGroup2);
        stateAnswer1 = findViewById(R.id.state1);
        stateAnswer2 = findViewById(R.id.state2);
        stateAnswer3 = findViewById(R.id.state3);
        stateAnswer4 = findViewById(R.id.state4);
        scoreTextView = findViewById(R.id.score);
        prompt = findViewById(R.id.prompt);
        questionNumber = findViewById(R.id.questionNumber);
        finalScore = findViewById(R.id.finalScore);
        resetButton = findViewById(R.id.resetButton);
        nextButton = findViewById(R.id.nextButton);
        textAnswer = findViewById(R.id.textAnswer);

        if (savedInstanceState == null) {
            // this path gets executed only once, at startup

            answers = new String[4];

            // get the data passed from MainActivity
            Intent intent = getIntent();
            quizLength = intent.getIntExtra(MainActivity.EXTRA_QUIZ_LENGTH, 10);
            mode = intent.getIntExtra(MainActivity.EXTRA_MODE, 0);

            // create the shuffled list of states
            List<String> strList = Arrays.asList(states);
            Collections.shuffle(strList);
            states = strList.toArray(new String[strList.size()]);

            nextButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.GONE);
            finalScore.setVisibility(View.GONE);

            loadQuestionData();
            updateUI();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putInt(QUIZ_LENGTH_KEY, quizLength);
        savedInstanceState.putInt(MODE, mode);
        savedInstanceState.putInt(SCORE, score);
        savedInstanceState.putInt(POS, pos);
        savedInstanceState.putBoolean(FINISHED, finished);
        savedInstanceState.putInt(IMG_RESOURCE, imgResource);
        savedInstanceState.putString(ANSWER, answer);
        savedInstanceState.putStringArray(STATES, states);
        savedInstanceState.putStringArray(ANSWERS, answers);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        quizLength = savedInstanceState.getInt(QUIZ_LENGTH_KEY);
        mode = savedInstanceState.getInt(MODE);
        score = savedInstanceState.getInt(SCORE);
        pos = savedInstanceState.getInt(POS);
        finished = savedInstanceState.getBoolean(FINISHED);
        imgResource = savedInstanceState.getInt(IMG_RESOURCE);
        answer = savedInstanceState.getString(ANSWER);
        states = savedInstanceState.getStringArray(STATES);
        answers = savedInstanceState.getStringArray(ANSWERS);

        // update the interface with the restored data
        updateUI();
    }

    /**
     * Convert each word of the string to upper case.
     *
     * @param in is the string to convert
     * @return converted string
     */
    private String capitalize(String in) {
        if (in.length() == 0)
            return "";
        
        String[] strArray = in.split(" ");

        // capitalize each word in the String array
        StringBuilder out = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1) + " ";
            out.append(cap);
        }

        return out.toString();
    }

    private RadioGroup.OnCheckedChangeListener changeGroup1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                stateRadioGroup2.setOnCheckedChangeListener(null);
                stateRadioGroup2.clearCheck();
                stateRadioGroup2.setOnCheckedChangeListener(changeGroup2);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener changeGroup2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                stateRadioGroup1.setOnCheckedChangeListener(null);
                stateRadioGroup1.clearCheck();
                stateRadioGroup1.setOnCheckedChangeListener(changeGroup1);
            }
        }
    };

    /**
     * Loads the data for the next question.
     */
    protected void loadQuestionData() {
        // get the next state in the list
        answer = states[pos];

        // get the resource ID that corresponds to the state name
        imgResource = getResources().getIdentifier(
                answer.replace(' ', '_'), // resources use '_' instead of ' '
                "drawable",
                "com.example.android.statesquiz");

        Log.i(TAG, "state=" + answer + ", resource=" + imgResource);

        // generate the list of answers
        int i, j, k;
        int len_states = states.length;
        answers[0] = answer;
        Random rand = new Random();
        do {
            i = rand.nextInt(len_states);
        } while (states[i].equals(answer));
        answers[1] = states[i];
        do {
            j = rand.nextInt(len_states);
        } while (j == i || states[j].equals(answer));
        answers[2] = states[j];
        do {
            k = rand.nextInt(len_states);
        } while (k == i || k == j || states[k].equals(answer));
        answers[3] = states[k];

        // shuffle the answers
        List<String> strList = Arrays.asList(answers);
        Collections.shuffle(strList);
        answers = strList.toArray(new String[strList.size()]);
    }

    /**
     * Update the user interface with the loaded quiz data
     */
    public void updateUI() {
        prompt.setText("");
        if (finished) {
            // the quiz is completed
            String msg = "You got " + score + " out of " + quizLength + " correct.";
            Toast.makeText(StateQuestionActivity.this, msg, Toast.LENGTH_SHORT).show();

            questionNumber.setText("");
            scoreTextView.setText("");

            String scoreMsg;
            if (score == quizLength)
                scoreMsg = "Perfect!\nYou got them all right.";
            else {
                int ratio = score * 100 / quizLength;
                String prefix;
                if (ratio >= 90)
                    prefix = "Almost there!";
                else if (ratio >= 80)
                    prefix = "Pretty good!";
                else if (ratio >= 70)
                    prefix = "Not bad!";
                else
                    prefix = "Keep practicing!";
                scoreMsg = prefix + "\nYou got " + score + " out of " + quizLength + " correct.";
            }
            finalScore.setVisibility(View.VISIBLE);
            finalScore.setText(scoreMsg);

            stateImageView.setImageResource(R.drawable.american_flag_flaming);
            fillInTheBlank.setVisibility(LinearLayout.GONE);
            multipleChoice.setVisibility(LinearLayout.GONE);
            nextButton.setVisibility(View.GONE);
            resetButton.setVisibility(View.VISIBLE);
        } else {
            String msg = "Score: " + score + "/" + quizLength;
            scoreTextView.setText(msg);

            // set the image
            stateImageView.setImageResource(imgResource);

            if (mode == 0) {
                fillInTheBlank.setVisibility(LinearLayout.GONE);
                multipleChoice.setVisibility(LinearLayout.VISIBLE);
            } else {
                fillInTheBlank.setVisibility(LinearLayout.VISIBLE);
                multipleChoice.setVisibility(LinearLayout.GONE);
            }

            // Clear the text answer
            textAnswer.setText("");

            // set the answers in the radio buttons
            stateAnswer1.setText(capitalize(answers[0]));
            stateAnswer2.setText(capitalize(answers[1]));
            stateAnswer3.setText(capitalize(answers[2]));
            stateAnswer4.setText(capitalize(answers[3]));

            String questionNumberMsg = "#" + (pos + 1);
            questionNumber.setText(questionNumberMsg);
        }
    }

    /**
     * Check if the answer is right.
     */
    public void nextQuestion(View view) throws InterruptedException {
        int delay;
        String stateText;

        if (mode == 0) {
            // get the checked IDs of each radio group
            int checkId1 = stateRadioGroup1.getCheckedRadioButtonId();
            int checkId2 = stateRadioGroup2.getCheckedRadioButtonId();

            if (checkId1 == -1 && checkId2 == -1)
                return;

            stateRadioGroup1.clearCheck();
            stateRadioGroup2.clearCheck();

            // determine the selected text
            int selection = checkId1 == -1 ? checkId2 : checkId1;
            RadioButton selectedRadioButton = findViewById(selection);
            stateText = ((String) selectedRadioButton.getText()).toLowerCase();
        } else {
            stateText = (textAnswer.getText().toString()).toLowerCase();
        }

        // check the answer
        String statusMsg;
        if (stateText.equals(answer.toLowerCase())) {
            statusMsg = "Correct!";
            prompt.setTextColor(Color.rgb(0, 255, 0));
            prompt.setText(statusMsg);
            delay = 750;
            score++;
        } else {
            statusMsg = "Incorrect.\nThis is " + capitalize(answer);
            prompt.setTextColor(Color.rgb(255, 0, 0));
            prompt.setText(statusMsg);
            delay = 2000; // make the delay a little longer to allow reading the correct answer
        }

        String scoreMsg = "Score: " + score + "/" + quizLength;
        scoreTextView.setText(scoreMsg);

        if (++pos < quizLength) {
            loadQuestionData();
        } else {
            finished = true;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        }, delay);

    }

    /**
     * Go back to MainActivity to start a new quiz.
     */
    public void reset(View view) {
        finish();
    }
}
