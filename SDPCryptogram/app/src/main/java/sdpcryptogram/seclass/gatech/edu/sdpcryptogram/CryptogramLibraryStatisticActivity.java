package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CryptogramLibraryStatisticActivity extends AppCompatActivity {
    //region member veriable
    private Context context;
    //endregion member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram_library_statistic);
        context = getApplicationContext();
        //load all the exsting cryptograms.
        try {
            CryptogramLibrary.loadCryptograms(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the data source
        List<Cryptogram> _cryptograms = CryptogramLibrary.listCryptograms(context);
        String[] listItems = new String[_cryptograms.size()];
        for (int i = 0; i< _cryptograms.size(); i++){
            Cryptogram c = _cryptograms.get(i);
            listItems[i] = c.getName() + " - " + c.getDateCreated().toString();
        }

        //create ArrayAdaptor
        ArrayAdapter listAdaptor= new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listItems);
        ListView listCryptograms = (ListView) findViewById(R.id.list_cryptograms);
        listCryptograms.setAdapter(listAdaptor);

        //create the listener for the ListView
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> listCryptograms,
                                            View view,
                                            int position,
                                            long id) {
                        //Pass the Cryptogram the user clicks on to the CryptogramStatistics
                        Intent intent = new Intent(CryptogramLibraryStatisticActivity.this,
                                CryptogramStatisticsActivity.class);
                        intent.putExtra(CryptogramStatisticsActivity.EXTRA_ID, (int) id);
                        startActivity(intent);
                    }
                };
        //Assign the listener to the list view
        listCryptograms.setOnItemClickListener(itemClickListener);
    }
}
