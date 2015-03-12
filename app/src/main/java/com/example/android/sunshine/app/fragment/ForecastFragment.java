package com.example.android.sunshine.app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.activity.DetailActivity;
import com.example.android.sunshine.app.tasks.FetchWeatherTask;

import java.util.ArrayList;

/**
 * Created by rodrigo on 24/02/15.
 */
public class ForecastFragment extends Fragment {

    private final String LOG_TAG = ForecastFragment.class.getSimpleName();
    private ArrayList<String> forecast = new ArrayList<String>(0);
    private ArrayAdapter<String> adapter;

    public ForecastFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ativa os itens de menu do fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Log.v(LOG_TAG, "onOptionsItemSelected:R.id.action_refresh");
            this.updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Cria o adapter usando o layout de lista list_item_forecast, passando a lista como parametro
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, forecast);

        //Obtem a View da lista ListView e insere o adapter criado
        ListView lw = (ListView) rootView.findViewById(R.id.listview_forecast);
        lw.setAdapter(adapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempo = adapter.getItem(position);

                //Tela de detalhes da previsao do tempo
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(Intent.EXTRA_TEXT, tempo);

                Log.d(LOG_TAG, "Start DetailActivity");
                startActivity(i);
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        //Chama o metodo para atualizar os dados
        this.updateWeather();
    }

    /**
     * Metodo utilizado para atualizar os dados do clima, executando ao criar um novo fragmento
     * @author rodrigo.arantes 20150303
     */
    private void updateWeather() {
        Log.v(LOG_TAG, "updateWeather()");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.settings_location_key), getString(R.string.settings_location_default));
        FetchWeatherTask f = new FetchWeatherTask(this.getActivity(), adapter);
        f.execute(location);
    }


}