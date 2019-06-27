package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.ToastWrapper;

/**
 * A screen that offers registration via username/password/firstname/lastname/email.
 * developed by Li Chen 7/3/2018
 */
public class RegisterActivity extends AppCompatActivity {
    // region Member Variables
    private Context context;
    // endregion Member Variables

    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = getApplicationContext();
        AccountManager.loadPlayers(context);
    }
    // endregion onCreate

    // region Public Methods
    /**
     * Registers the player
     * @param view View
     */
    public void createPlayer(View view) {
        EditText firstNameEdit = findViewById(R.id.firstname);
        EditText lastNameEdit = findViewById(R.id.lastname);
        EditText userNameEdit = findViewById(R.id.username);
        EditText emailEdit = findViewById(R.id.email);
        EditText passwordEdit = findViewById(R.id.password);

        if (!isRequiredInfoValid(firstNameEdit, lastNameEdit, emailEdit, passwordEdit, userNameEdit)) {
            return;
        }

        Player new_player = new Player(
                firstNameEdit.getText().toString(),
                lastNameEdit.getText().toString(),
                emailEdit.getText().toString(),
                passwordEdit.getText().toString(),
                userNameEdit.getText().toString());
        AccountManager.addPlayer(new_player);
        AccountManager.savePlayers(context);

        ToastWrapper.shortToast(context, "Player " + userNameEdit.getText().toString() + " has been created!\nPlease log in.");

        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
    // endregion Public Methods

    // region Private Methods
    /**
     * Checks the required fields and sets the appropriate errors
     * @param firstNameEdit First Name edit field
     * @param lastNameEdit Last Name edit field
     * @param emailEdit Email edit field
     * @param passwordEdit Password edit field
     * @param userNameEdit Username edit field
     * @return True if there are no validation errors, otherwise false
     */
    private boolean isRequiredInfoValid(EditText firstNameEdit, EditText lastNameEdit, EditText emailEdit, EditText passwordEdit, EditText userNameEdit) {
        if (firstNameEdit.getText().toString().equals("")) {
            firstNameEdit.setError(getText(R.string.register_first_name_required));
        }

        if (lastNameEdit.getText().toString().equals("")) {
            lastNameEdit.setError(getText(R.string.register_last_name_required));
        }

        if (emailEdit.getText().toString().equals("")) {
            emailEdit.setError(getText(R.string.register_email_required));
        } else if (!emailEdit.getText().toString().contains("@") ||
                !emailEdit.getText().toString().contains(".")) {
            emailEdit.setError(getText(R.string.register_email_invalid));
        }

        if (userNameEdit.getText().toString().equals("")) {
            userNameEdit.setError(getText(R.string.register_username_required));
        }

        if (passwordEdit.getText().toString().equals("")) {
            passwordEdit.setError(getText(R.string.register_password_required));
        } else if (passwordEdit.getText().toString().length() < 4){
            passwordEdit.setError(getText(R.string.register_password_too_short));
        }

        if (!AccountManager.isUserNameAvailable(userNameEdit.getText().toString())){
            userNameEdit.setError(getText(R.string.register_username_taken));
        }

        return firstNameEdit.getError() == null &&
                lastNameEdit.getError() == null &&
                emailEdit.getError() == null &&
                userNameEdit.getError() == null &&
                passwordEdit.getError() == null;
    }
    // endregion Private Methods
}