package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import java.io.IOException;

/**
 * A screen that offers registration via username/password/firstname/lastname/email.
 * developed by Li Chen 7/3/2018
 */
public class MainActivity extends AppCompatActivity {
    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        generateCryptogramLibrary(context);
        if (AccountManager.getNumberOfAccounts() == 0) {
            generateDummyUsers(context);
            CryptogramLibrary.saveCryptograms(getApplicationContext());
        }

        setContentView(R.layout.activity_main);
    }
    // endregion onCreate

    /**
     * Redirects to the LoginActivity
     * @param view View
     */
    public void redirectLogin(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * Redirects to the RegisterActivity
     * @param view View
     */
    public void redirectRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    // region Private Methods
    /**
     * Generates the cryptogram if no cryptograms were found (new install)
     * @param context Application context
     */
    private void generateCryptogramLibrary(Context context) {
        try {
            CryptogramLibrary.loadCryptograms(context);
            if (CryptogramLibrary.listCryptograms(context).size() == 0) {
                CryptogramLibrary.instantiateNewCryptogramLibrary(context);
            }
        } catch (IOException e) {
            CryptogramLibrary.instantiateNewCryptogramLibrary(context);
        }
    }

    /**
     * Generates the dummy user if no users were found (new install)
     * @param context Application context
     */
    private void generateDummyUsers(Context context) {
        AccountManager.loadPlayers(context);

        if (AccountManager.getNumberOfAccounts() == 0) {
            Player dummyPlayer = new Player("Fake", "User", "fake@email.com", "secure", "FakeUser");
            AccountManager.setActivePlayer(dummyPlayer);
            PlayedCryptogram playedCryptogram = CryptogramLibrary.getCryptogram("Another easy one").play();
            playedCryptogram.setNumberOfAttempt(0);
            playedCryptogram.solve("doesn't matter");
            dummyPlayer.addPlayedCryptogram(playedCryptogram);

            PlayedCryptogram solvedCryptogram = CryptogramLibrary.getCryptogram("My first cryptogram!").play();
            solvedCryptogram.setNumberOfAttempt(0);
            solvedCryptogram.solve("Hello World");
            dummyPlayer.addPlayedCryptogram(solvedCryptogram);

            PlayedCryptogram unsolvedCryptogram = CryptogramLibrary.getCryptogram("Only one chance!").play();
            unsolvedCryptogram.setNumberOfAttempt(0);
            unsolvedCryptogram.solve("can't figure it out");
            dummyPlayer.addPlayedCryptogram(unsolvedCryptogram);

            AccountManager.setActivePlayer(null);
            AccountManager.addPlayer(dummyPlayer);
            AccountManager.savePlayers(context);
        }
    }
    // endregion Private Methods
}
