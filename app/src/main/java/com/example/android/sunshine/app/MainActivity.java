package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        ArrayList<String> forecast = new ArrayList<String>(0);

        public PlaceholderFragment() {
            forecast.add("Hoje - Sol - 88/63");
            forecast.add("Amanha - Neblina - 88/63");
            forecast.add("Quarta - Nublado - 70/63");
            forecast.add("Quinta - Ensolarado - 58/60");
            forecast.add("Sexta - Chuvoso - 58/60");
            forecast.add("Sab - Sol - 10/11");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //Cria o adapter usando o layout de lista list_item_forecast, passando a lista como parametro
            ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                                                                    R.id.list_item_forecast_textview, forecast);

            //Obtem a View da lista ListView e insere o adapter criado
            ListView lw = (ListView) rootView.findViewById(R.id.listview_forecast);
            lw.setAdapter(adap);


            return rootView;
        }
    }
}

