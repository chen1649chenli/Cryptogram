package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.ToastWrapper;

public class PlayCryptogramActivity extends AppCompatActivity {
    // region Member Variables
    private final int MAX_CHARACTERS_PER_ROW = 15;
    private EditText _activeEditText;
    private LinearLayout _createCipherLayout;
    private PlayedCryptogram _playedCryptogram;
    private boolean _updatingProgrammatically;
    // endregion Member Variables

    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_cryptogram);
        _createCipherLayout = findViewById(R.id.createCipherViewParent);
        final Context context = getApplicationContext();

        Intent intent = getIntent();
        String cryptogramName = intent.getStringExtra(CryptogramLibraryActivity.EXTRA_TEXT);

        _playedCryptogram = AccountManager.getActivePlayer().getPlayedCryptogram(CryptogramLibrary.getCryptogram(cryptogramName));
        String EncodedPhrase = _playedCryptogram.getEncodedPhrase();

        ((TextView) findViewById(R.id.puzzleName)).setText(cryptogramName);

        updatePlayCountLabel(_playedCryptogram);

        String currentText = EncodedPhrase;

        _updatingProgrammatically = true;
        int perRowCounter = 0;

        LinearLayout horizontalLayout = new LinearLayout(context);

        for (char c : currentText.toCharArray()) {
            if (perRowCounter % MAX_CHARACTERS_PER_ROW == 0) {
                horizontalLayout = new LinearLayout(context);
                _createCipherLayout.addView(horizontalLayout);
                perRowCounter = 0;
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout cipherPair = new LinearLayout(context);
            cipherPair.setOrientation(LinearLayout.VERTICAL);
            cipherPair.setLayoutParams(params);

            TextView solution = new TextView(context);
            solution.setLayoutParams(params);

            EditText code = new EditText(context);
            code.setLayoutParams(params);
            code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            code.addTextChangedListener(generateCipherTextWatcher());
            code.setTag(Character.toString(c));
            code.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    _activeEditText = (EditText) v;
                    String target = ((TextView) ((LinearLayout) _activeEditText.getParent()).getChildAt(1)).getText().toString();
                    updateMatchingEditBackgrounds(target);
                    v.performClick();
                    return false;
                }
            });
            code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    _activeEditText = (EditText) v;
                    String target = ((TextView) ((LinearLayout) _activeEditText.getParent()).getChildAt(1)).getText().toString();
                    updateMatchingEditBackgrounds(target);
                }

            });

            if (!(c >= 65 && c <= 90) && !(c >= 97 && c <= 122)) {
                code.setEnabled(false);
                code.setText(String.valueOf(c));
            }

            solution.setText(Character.toString(c));

            cipherPair.addView(code);
            solution.setTypeface(Typeface.MONOSPACE);
            solution.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            cipherPair.addView(solution);

            cipherPair.setTag("pair");

            horizontalLayout.addView(cipherPair);
            perRowCounter++;
        }
        _updatingProgrammatically = false;

        _activeEditText = (EditText) ((LinearLayout) _createCipherLayout.getChildAt(0)).getChildAt(0);
    }
    // endregion onCreate

    // region Public Methods
    /**
     * Attempts to solve the cryptogram
     * @param view View
     */
    public void onSubmit(View view){
        String attemptedGuess = getGuess();

        if (!isCipherComplete()) {
            ToastWrapper.shortToast(getApplicationContext(), getString(R.string.play_cryptogram_cipher_incomplete));

            return;
        }

       if (attemptToSolve(attemptedGuess)) {
           finish();
           startActivity(new Intent(this, CryptogramLibraryActivity.class));
           return;
        }

        Cryptogram cryptogram = CryptogramLibrary.getCryptogram(_playedCryptogram.getName());
        int numberOfAttempts = AccountManager.getActivePlayer().getCombinedPlayedCryptogram(cryptogram).getNumberOfAttempt() + 1;
        int maxAttempts = cryptogram.getMaxAttempts();
        _playedCryptogram.setNumberOfAttempt(numberOfAttempts);

        if (numberOfAttempts < maxAttempts) {
            _playedCryptogram = CryptogramLibrary.getCryptogram(_playedCryptogram.getName()).play();
            AccountManager.getActivePlayer().addPlayedCryptogram(_playedCryptogram);
            AccountManager.savePlayers(getApplicationContext());

            ToastWrapper.shortToast(getApplicationContext(), getString(R.string.play_cryptogram_incorrect_attempt));

            updatePlayCountLabel(_playedCryptogram);
        } else {
            ToastWrapper.longToast(getApplicationContext(), getString(R.string.play_cryptogram_no_remaining_attempts));
            finish();
            startActivity(new Intent(this, CryptogramLibraryActivity.class));
        }
    }
    // endregion Public Methods

    // region Private Methods
    /**
     * Attempts to solve the cryptogram based on the provided guess
     * @param guess Guess to attempt
     * @return True if the guess was correct, otherwise false
     */
    private boolean attemptToSolve(String guess) {
        if (_playedCryptogram.solve(guess)) {
            ToastWrapper.longToast(getApplicationContext(), getString(R.string.play_cryptogram_correct_attempt));

            _playedCryptogram.setSolved(true);

            AccountManager.savePlayers(getApplicationContext());
            CryptogramLibrary.saveCryptograms(getApplicationContext());

            return true;
        }

        return false;
    }

    /**
     * Generates the TextWatcher for updating each of the cipher EditText objects
     * @return TextWatcher for cipher editing
     */
    private TextWatcher generateCipherTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (_updatingProgrammatically) { return; }

                LinearLayout layout = (LinearLayout) _activeEditText.getParent();
                TextView label = (TextView) layout.getChildAt(1);
                updateAllCharacters(label.getText(), s);
            }
        };
    }

    /**
     * Retrieves the guess from the attempted cipher mapping on the view
     * @return Guess from the view
     */
    private String getGuess() {
        String guess = "";

        for (int i = 0; i < _createCipherLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) _createCipherLayout.getChildAt(i);

            for (int j = 0; j < row.getChildCount(); j++) {
                EditText edit = (EditText) ((LinearLayout) row.getChildAt(j)).getChildAt(0);

                guess += edit.getText().toString();
            }
        }

        return guess;
    }

    /**
     * Determines if the cipher is completely entered
     * @return True if all fields are entered, otherwise false
     */
    private boolean isCipherComplete() {
        boolean complete = true;

        for (int i = 0; i < _createCipherLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) _createCipherLayout.getChildAt(i);

            for (int j = 0; j < row.getChildCount(); j++) {
                EditText edit = (EditText) ((LinearLayout) row.getChildAt(j)).getChildAt(0);

                if (edit.getText().toString().equals("")) { complete = false; }
            }
        }

        return complete;
    }

    /**
     * Check if character is upper case
     * @param input Character to check
     * @return is Upper case
     */
    private boolean isCharUpperCase(char input)
    {
        //if char from solution i is uppercase
        if (input >= 65 && input <= 90) {
            return true;
        }

        return false;
    }


    /**
     * Updates all of the EditText elements that match the specified character
     * @param targetChar Ciphered character to update EditText elements for
     * @param newCharacter Character to set
     */
    private void updateAllCharacters(CharSequence targetChar, Editable newCharacter) {
        _updatingProgrammatically = true;

        for (int i = 0; i < _createCipherLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) _createCipherLayout.getChildAt(i);

            for (int j = 0; j < row.getChildCount(); j++) {
                TextView label = (TextView) ((LinearLayout) row.getChildAt(j)).getChildAt(1);
                EditText edit = (EditText) ((LinearLayout) row.getChildAt(j)).getChildAt(0);

                if (label.getText().toString().toUpperCase().equals(targetChar.toString().toUpperCase())) {

                    char labelChar = label.getText().charAt(0);
                    edit.setText(
                            isCharUpperCase(labelChar) ?
                                    newCharacter.toString().toUpperCase() :
                                    newCharacter.toString().toLowerCase());
                }
            }
        }
        _updatingProgrammatically = false;
    }

    /**
     * Updates all matching character EditText backgrounds
     * @param targetChar Character labels to change the EditText backgrounds for
     */
    private void updateMatchingEditBackgrounds(CharSequence targetChar) {
        for (int i = 0; i < _createCipherLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) _createCipherLayout.getChildAt(i);

            for (int j = 0; j < row.getChildCount(); j++) {
                TextView label = (TextView) ((LinearLayout) row.getChildAt(j)).getChildAt(1);
                EditText edit = (EditText) ((LinearLayout) row.getChildAt(j)).getChildAt(0);

                if (label.getText().toString().equals(targetChar)) {
                    edit.getBackground().setColorFilter(getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
                } else {
                    edit.getBackground().clearColorFilter();
                }
            }
        }
    }

    /**
     * Updates the play count label
     * @param cryptogram Cryptogram being played
     */
    private void updatePlayCountLabel(Cryptogram cryptogram) {
        TextView attemptsTextView = findViewById(R.id.numberAttemptsValue2);

        int numberOfAttempts = AccountManager.getActivePlayer().getCombinedPlayedCryptogram(cryptogram).getNumberOfAttempt() + 1;

        String stringToSet = getString(R.string.play_cryptogram_attempts, numberOfAttempts, cryptogram.getMaxAttempts());
        attemptsTextView.setText(stringToSet);

        if (numberOfAttempts == cryptogram.getMaxAttempts()) {
            attemptsTextView.setTextColor(getColor(R.color.colorRed));
            attemptsTextView.setTypeface(null, Typeface.BOLD);
        }
    }
    // endregion Private Methods
}