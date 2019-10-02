package com.devsoft.countryapi_recyclersearch;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Country> data = new ArrayList<>();
    RequestQueue queue;
    ProgressDialog dialog;
    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("You Are Disconnected, Turn On Mobile Data & Restart App.");
        dialog.setCancelable(false);
        fatchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.search,menu);

        MenuItem item = menu.findItem(R.id.search_btn);
         SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void fatchData() {
        dialog.show();
        data.clear();
        String url = "https://restcountries.eu/rest/v2/all";

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for(int i=0;i<response.length();i++){
                        JSONObject country = response.getJSONObject(i);
                        String name = country.getString("name");
                        String alpha2Code = country.getString("alpha2Code");
                        String capital = country.getString("capital");
                        String region = country.getString("region");
                        Integer population = country.getInt("population");
                        Integer area;
                        try{
                            area = country.getInt("area");
                        }catch(Exception ex){
                            area = 0;
                        }
                        JSONArray timezones = country.getJSONArray("timezones");
                        ArrayList<String> timezone = new ArrayList<>();
                        for(int j=0;j<timezones.length();j++){
                            timezone.add(timezones.getString(j));
                        }
                        JSONArray currenci = country.getJSONArray("currencies");
                        ArrayList<Currency> currencies = new ArrayList<>();
                        for(int k=0;k<currenci.length();k++){
                            JSONObject obj = currenci.getJSONObject(k);
                            String code = obj.getString("code");
                            String name1 = obj.getString("name");

                            currencies.add(new Currency(name1,code));
                        }
                        String flag = country.getString("flag");
                        data.add(new Country(name,alpha2Code,capital,region,flag,population,area,timezone,currencies));
                    }
                    RecyclerView recyclerView = findViewById(R.id.recyclerview);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
                    adapter = new myAdapter(data,MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    dialog.dismiss();


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
