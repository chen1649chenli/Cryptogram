package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CryptogramLibraryActivity extends AppCompatActivity {
    // region Member Variables
    private Cryptogram _cryptogramDisplayed;
    public static final String EXTRA_TEXT = "EXTRA_TEXT";
    // endregion Member Variables

    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram_library);

        findViewById(R.id.playButton).setEnabled(false);
        populateScroller(getApplicationContext());
    }
    // endregion onCreate

    // region Public Methods
    /**
     * Plays the selected cryptogram
     * @param view View
     */
    public void playCryptogram(View view) {
        PlayedCryptogram playedCryptogram = _cryptogramDisplayed.play();
        AccountManager.getActivePlayer().addPlayedCryptogram(playedCryptogram);
        AccountManager.savePlayers(getApplicationContext());
        finish();

        String cryptoNAME = _cryptogramDisplayed.getName().toString();
        //Pass the Cryptogram the user clicks on to the PlayCryptoActivity
        Intent intent = new Intent( CryptogramLibraryActivity.this, PlayCryptogramActivity.class);
        intent.putExtra(EXTRA_TEXT, cryptoNAME);
        startActivity(intent);
    }
    // endregion Public Methods

    // region Private Methods
    /**
     * Populates the scroller based on the CryptogramLibrary contents
     */
    private void populateScroller(Context context) {
        List<Cryptogram> cryptograms = CryptogramLibrary.listCryptograms(context);
        final LinearLayout layout = this.findViewById(R.id.linearLayoutContent);

        for (final Cryptogram cryptogram : cryptograms) {
            if (!AccountManager.getActivePlayer().hasPlaysRemaining(cryptogram)) { continue; }

            TextView textView = new TextView(this);
            textView.setText(cryptogram.getName());

            textView.setTextSize(25);
            textView.setPadding(2, 20, 2, 20);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView highlightedTextView = layout.findViewWithTag("highlighted");
                    if (highlightedTextView != null) {
                        highlightedTextView.setBackgroundResource(0);
                        highlightedTextView.setTag("");
                    }

                    v.setTag("highlighted");
                    v.setBackgroundResource(R.color.colorAccent);
                    updateCryptogramDetails(cryptogram);
                }
            });

            layout.addView(textView);
        }
    }

    /**
     * Updates the cryptogram details pane
     * @param cryptogram Cryptogram to populate details with
     */
    private void updateCryptogramDetails(Cryptogram cryptogram) {
        ((TextView) findViewById(R.id.dateCreatedValue)).setText(cryptogram.getDateCreated().toString());
        ((TextView) findViewById(R.id.totalSolvesValue)).setText("" + cryptogram.getNumberOfSolves());
        boolean playedCryptogram = AccountManager.getActivePlayer().hasPlayedCryptogram(cryptogram);
        int numberOfAttempts = 0;
        if (playedCryptogram) {
            numberOfAttempts = AccountManager.getActivePlayer().getCombinedPlayedCryptogram(cryptogram).getNumberOfAttempt() + 1;
        }

        ((TextView) findViewById(R.id.numberAttemptsValue)).setText(numberOfAttempts + "/" + cryptogram.getMaxAttempts());
        findViewById(R.id.playButton).setEnabled(true);
        _cryptogramDisplayed = cryptogram;

        updatePlayCryptogramButton(cryptogram);
    }

    /**
     * Updates the Play Cryptogram button based on logic to determine if the player can attempt
     * any further plays on the puzzle
     * @param cryptogram Cryptogram puzzle
     */
    private void updatePlayCryptogramButton(Cryptogram cryptogram) {
        if (!AccountManager.getActivePlayer().hasPlayedCryptogram(cryptogram)) {
            findViewById(R.id.playButton).setEnabled(true);
            return;
        }

        findViewById(R.id.playButton).setEnabled(AccountManager.getActivePlayer().hasPlaysRemaining(cryptogram) &&
                !AccountManager.getActivePlayer().getPlayedCryptogram(cryptogram).getSolved());
    }
    // endregion Private Methods
}