package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedHashSet;
import java.util.Set;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.cipher.Cipher;
import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.ToastWrapper;

public class CreateCryptogramActivity extends AppCompatActivity {
    // region Member Variables
    private final int MAX_CHARACTERS_PER_ROW = 15;
    private Cipher _cipher;
    private Integer _maxAttempts;
    // endregion Member Variables

    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cryptogram);

        ((EditText) findViewById(R.id.solutionEdit)).addTextChangedListener(generateSolutionEditTextWatcher());
        ((EditText) findViewById(R.id.solutionEdit)).addTextChangedListener(generateBlurDisableContinueTextWatcher());
        ((EditText) findViewById(R.id.puzzleNameEdit)).addTextChangedListener(generateBlurDisableContinueTextWatcher());
        ((EditText) findViewById(R.id.maxAttemptsEdit)).addTextChangedListener(generateBlurDisableContinueTextWatcher());
    }
    // endregion onCreate

    // region Public Methods
    /**
     * Validates the inputs on the required fields and prepares the user for puzzle creation
     * @param view View
     */
    public void onViewEncodedClick(View view) {
        EditText solutionEditText = findViewById(R.id.solutionEdit);
        boolean canCreate = true;

        canCreate = canCreate & checkPuzzleName((EditText) findViewById(R.id.puzzleNameEdit));
        canCreate = canCreate & checkSolution(solutionEditText);
        canCreate = canCreate & checkMaxAttemptCount((EditText) findViewById(R.id.maxAttemptsEdit));
        canCreate = canCreate & checkCipher();

        setContinueButton(canCreate);

        if (canCreate) {
            String encodedSolution = _cipher.encode(solutionEditText.getText().toString());
            ((TextView) findViewById(R.id.createEncodedView)).setText(encodedSolution);
        } else {
            ((TextView) findViewById(R.id.createEncodedView)).setText("");
        }
    }

    /**
     * Finalizes puzzle creation
     * @param view View
     */
    public void onContinueClick(View view) {
        Cryptogram crypto = new Cryptogram(
                ((EditText) findViewById(R.id.puzzleNameEdit)).getText().toString(),
                _maxAttempts,
                _cipher,
                ((EditText) findViewById(R.id.solutionEdit)).getText().toString());

        CryptogramLibrary.createCryptogram(crypto);
        CryptogramLibrary.saveCryptograms(getApplicationContext());

        ToastWrapper.shortToast(getApplicationContext(), getText(R.string.create_cryptogram_created_successfully).toString());

        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
    // endregion Public Methods

    // region Private Methods
    /**
     * Validates that the cipher is valid and complete
     * @return True if the cipher is valid, otherwise false
     */
    private boolean checkCipher() {
        boolean cipherValid = updateCipher();

        if (!cipherValid) {
            ToastWrapper.shortToast(getApplicationContext(), getText(R.string.create_cryptogram_cipher_incomplete).toString());
            return false;
        }

        return true;
    }

    /**
     * Validates the Max Attempt
     * @param maxAttemptEditText Max attempt EditText
     * @return True if the max attempt provided is valid, otherwise false
     */
    private boolean checkMaxAttemptCount(EditText maxAttemptEditText) {
        String maxCountValue = maxAttemptEditText.getText().toString();
        if (maxCountValue.equals("")) {
            maxAttemptEditText.setError(getText(R.string.create_cryptogram_error_max_attempts_missing));
            return false;
        }

        int maxCountInt = Integer.parseInt(maxCountValue);

        if (maxCountInt <= 0) {
            maxAttemptEditText.setError(getText(R.string.create_cryptogram_error_max_attempts_below_zero));
            return false;
        }

        _maxAttempts = maxCountInt;
        return true;
    }

    /**
     * Validates that the puzzle name is valid and unique
     * @param puzzleNameEditText Puzzle name EditText
     * @return True if the puzzle name is valid and unique, otherwise false
     */
    private boolean checkPuzzleName(EditText puzzleNameEditText) {
        String puzzleName = puzzleNameEditText.getText().toString();
        if (puzzleName.equals("")) {
            puzzleNameEditText.setError(getText(R.string.create_cryptogram_error_puzzle_name_missing));
            return false;
        }

        if (!CryptogramLibrary.isUnique(puzzleName)) {
            puzzleNameEditText.setError(getText(R.string.create_cryptogram_error_puzzle_name_not_unique));
            return false;
        }

        return true;
    }

    /**
     * Validates the solution provided
     * @param solutionEditText Solution EditText
     * @return True if the solution is valid, otherwise false
     */
    private boolean checkSolution(EditText solutionEditText) {
        if (solutionEditText.getText().toString().equals("")) {
            solutionEditText.setError(getText(R.string.create_cryptogram_error_solution_missing));
            return false;
        }

        return true;
    }

    /**
     * Generates a TextWatcher that disables the Continue button on blur, forcing the user to
     * validate after any changes
     * @return TextWatcher that disables the Continue button on EditText blur
     */
    private TextWatcher generateBlurDisableContinueTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setContinueButton(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /**
     * Generates the TextWatcher instance for the solution EditText
     * @return TextWatcher instance unique for the solution EditText
     */
    private TextWatcher generateSolutionEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LinearLayout createCipherLayout = findViewById(R.id.createCipherView);
                createCipherLayout.removeAllViews();

                Context context = getApplicationContext();

                String currentText = s.toString().toUpperCase();
                String uniqueChars = getUniqueCharacters(currentText);

                LinearLayout horizontalLayout = new LinearLayout(context);

                int perRowCounter = 0;
                for (char c : uniqueChars.toCharArray()) {
                    if (perRowCounter % MAX_CHARACTERS_PER_ROW == 0) {
                        horizontalLayout = new LinearLayout(context);
                        createCipherLayout.addView(horizontalLayout);
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
                    code.setFilters(new InputFilter[] {
                            new InputFilter() {
                                @Override
                                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                                    if (source.equals("") || source.toString().matches("[a-zA-Z]")) {
                                        return source;
                                    }
                                    return "";
                                }
                            },
                            new InputFilter.LengthFilter(1)
                            });
                    code.addTextChangedListener(generateBlurDisableContinueTextWatcher());

                    solution.setText(Character.toString(c));
                    cipherPair.addView(solution);
                    cipherPair.addView(code);
                    horizontalLayout.addView(cipherPair);
                    perRowCounter++;
                }
            }
        };
    }

    /**
     * Scans the solution phrase for all unique characters
     * @param input Solution input from the activity
     * @return String of all unique characters
     */
    private String getUniqueCharacters(String input) {
        Set<Character> inputSet = new LinkedHashSet<>();

        StringBuilder output = new StringBuilder();

        for(char inputChar : input.toCharArray())
        {
            if(Character.isAlphabetic(inputChar))
            {
                inputSet.add(inputChar);
            }

        }

        for(char cc : inputSet)
        {
            output.append(cc);
        }

        return output.toString();
    }

    /**
     * Enables or disables the Continue button
     * @param enable Boolean to determine if the button should be enabled
     */
    private void setContinueButton(boolean enable) {
        findViewById(R.id.continueButton).setEnabled(enable);
        findViewById(R.id.continueButton).setBackgroundResource(
                enable ?
                        R.color.colorPrimary :
                        R.color.colorDisabled);
    }

    /**
     * Updates the cipher based on the provided char -> char mappings on the activity
     * @return True if the cipher was completely entered without duplicates, otherwise false
     */
    private boolean updateCipher() {
        LinearLayout createCipherLayout = findViewById(R.id.createCipherView);
        _cipher = new Cipher();

        boolean cipherComplete = true;

        for (int i = 0; i < createCipherLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) createCipherLayout.getChildAt(i);

            for (int j = 0; j < row.getChildCount(); j++) {
                View solutionCharView = ((LinearLayout)row.getChildAt(j)).getChildAt(0);
                View encodedCharView = ((LinearLayout)row.getChildAt(j)).getChildAt(1);

                String solutionChar = ((TextView) solutionCharView).getText().toString();
                String cipherEncodedChar = ((EditText) encodedCharView).getText().toString();



                if (!solutionChar.isEmpty() && !cipherEncodedChar.isEmpty()) {
                    boolean addedKeyMapCipher = _cipher.addCipherCharPair(solutionChar.charAt(0), cipherEncodedChar.charAt(0));
                    if (!addedKeyMapCipher) {
                        return false;
                    }
                } else {
                    cipherComplete = false;
                }
            }
        }

        return cipherComplete;
    }
    // endregion Private Methods
}