package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ViewCompletedCryptogramsActivity extends AppCompatActivity {
    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_completed_cryptograms);

        populateCompletedCryptograms();
    }
    // endregion onCreate

    // region Private Methods
    /**
     * Populates the Completed Cryptogram list
     */
    private void populateCompletedCryptograms() {
        final LinearLayout layout = findViewById(R.id.completedCryptogramsLayout);
        List<PlayedCryptogram> playedCryptograms = AccountManager.getActivePlayer().getCombinedPlayedCryptograms();

        for (PlayedCryptogram cryptogram : playedCryptograms) {
            TextView textView = new TextView(this);
            textView.setText(cryptogram.getName());

            textView.setTextSize(18);
            textView.setPadding(2, 20, 2, 20);

            String status;
            if (cryptogram.getSolved()) {
                status = "Solved";
            } else if (cryptogram.getNumberOfAttempt() + 1 >= cryptogram.getMaxAttempts()) {
                status = "Completed";
            } else {
                status = "Played";
            }

            textView.setText(cryptogram.getName() + " - " + status + " - " + cryptogram.getDateCompleted().toString());
            layout.addView(textView);
        }
    }
    // endregion Private Methods
}