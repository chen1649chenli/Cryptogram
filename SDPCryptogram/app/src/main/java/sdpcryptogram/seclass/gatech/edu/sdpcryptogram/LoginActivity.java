package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * A screen that offers registration via username/password/firstname/lastname/email.
 * developed by Li Chen 7/3/2018
 */
public class LoginActivity extends AppCompatActivity {
    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((EditText) findViewById(R.id.password)).addTextChangedListener(getPasswordTextWatcher());
        AccountManager.loadPlayers(getApplicationContext());
    }
    // endregion onCreate

    // region Public Methods
    /**
     * Overrides the onBackPressed for some manageable session handling
     */
    @Override
    public void onBackPressed() {
        if (AccountManager.getActivePlayer() != null)
            super.onBackPressed();
        else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
     * Attempts to log in to the system
     * @param view Current view
     */
    public void login(View view) {
        EditText usernameEdit = findViewById(R.id.username);
        EditText passwordEdit = findViewById(R.id.password);

        if (usernameEdit.getText().toString().equals("")) {
            usernameEdit.setError(getText(R.string.login_error_username_required));
        }
        if (passwordEdit.getText().toString().equals("")) {
            passwordEdit.setError(getText(R.string.login_error_password_required));
        }
        if (usernameEdit.getError() != null || passwordEdit.getError() != null) { return; }

        if (!AccountManager.isUserNameAvailable(usernameEdit.getText().toString())) {
            Player login_player = AccountManager.getPlayerByUsername(usernameEdit.getText().toString());
            if (login_player.getPassword().equals(passwordEdit.getText().toString())) {
                AccountManager.setActivePlayer(login_player);

                finish();
                startActivity(new Intent(this, HomeActivity.class));
                return;
            }
        }

        usernameEdit.setError(getText(R.string.login_error_invalid_credentials));
    }
    // endregion Public Methods

    // region Private Methods
    /**
     * Generates the password TextWatcher listener
     * @return TextWatcher
     */
    private TextWatcher getPasswordTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EditText username = findViewById(R.id.username);
                if (username.getError() != null && username.getError().equals(getText(R.string.login_error_invalid_credentials))) {
                    username.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }
    // endregion Private Methods
}