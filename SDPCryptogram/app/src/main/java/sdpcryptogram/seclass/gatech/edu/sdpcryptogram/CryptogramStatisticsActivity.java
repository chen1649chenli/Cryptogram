package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.DateWrapper;

public class CryptogramStatisticsActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "cryptogramId";
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram_statistics);
        context = getApplicationContext();
        try {
            CryptogramLibrary.loadCryptograms(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the Cryptogram from the intent
        int cryptogramID = (Integer)getIntent().getExtras().get(EXTRA_ID);
        List<Cryptogram> _cryptograms = CryptogramLibrary.listCryptograms(context);
        Cryptogram _cryptogram = _cryptograms.get(cryptogramID);
        String title = _cryptogram.getName();
        DateWrapper date = _cryptogram.getDateCreated();
        int _numOfSolver = _cryptogram.getNumberOfSolves();
        List<Player> topSolvers = _cryptogram.getFirstSolvers(3);

        //Populate the puzzle name
        TextView puzzle_title = findViewById(R.id.puzze_title);
        puzzle_title.setText(title);

        //Populate the creation date
        TextView creation_date = findViewById(R.id.create_date);
        creation_date.setText("Creation Date - " + date);

        //Populate the solve number
        TextView solve_num = findViewById(R.id.total_solves);
        solve_num.setText(getString(R.string.cryptogram_statistics_total_solves, _numOfSolver));

        //populate the top three solvers
        int num = topSolvers.size();
        String[] listItems = new String[num];
        for (int i = 0; i < num; i++ ){
            Player _player = topSolvers.get(i);
            String name = _player.getUsername();
            PlayedCryptogram _playedCryptogram = _player.getPlayedCryptogram(_cryptogram);
            listItems[i] = name + "    " + _playedCryptogram.getDateCompleted().toString();
        }

        //create ArrayAdaptor
        ArrayAdapter listAdaptor= new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listItems);
        ListView listTopSolver = findViewById(R.id.top_solvers);
        listTopSolver.setAdapter(listAdaptor);

    }
}
