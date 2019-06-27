package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void redirectCreateCryptogram(View view) {
        Intent intent = new Intent(this, CreateCryptogramActivity.class);
        startActivity(intent);
    }

    public void redirectCryptogramLibraryList(View view) {
        Intent intent = new Intent(this, CryptogramLibraryActivity.class);
        startActivity(intent);
    }

    public void redirectStartScreen(View view) {
        AccountManager.setActivePlayer(null);
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void redirectCryptogramLibraryStatistics(View view) {
        Intent intent = new Intent(this, CryptogramLibraryStatisticActivity.class);
        startActivity(intent);
    }

    /**
     * Redirects to the View Completed Cryptograms screen
     * @param view View
     */
    public void redirectViewCompletedCryptograms(View view) {
        startActivity(new Intent(this, ViewCompletedCryptogramsActivity.class));
    }
}
