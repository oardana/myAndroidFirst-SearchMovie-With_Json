package com.example.makejson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private String JSON_URL;
    List<MovieModelClass> movieList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recylerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar,menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Cari Disini");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                JSON_URL = "http://www.omdbapi.com/?apikey=4cb6197c&s="+query+"";
                new GetData().execute();
                movieList.clear();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(MainActivity.this, "onMenuItemActionExpand dipanggil", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(MainActivity.this, "onMenuItemActionCollapse dipanggil", Toast.LENGTH_SHORT).show();
                movieList.clear();
                recyclerView.setAdapter(null);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public class GetData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) new URL(JSON_URL).openConnection();
                InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                int data = isr.read();
                while(data != -1){
                    current += (char) data;
                    data= isr.read();
                }
                return current;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("Search");
                for (int i = 0; i< jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    MovieModelClass movie = new MovieModelClass();
                    movie.setId(jsonObject1.getString("Type"));
                    movie.setName(jsonObject1.getString("Title"));
                    movie.setImg(jsonObject1.getString("Poster"));
                    movieList.add(movie);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            PutDataIntoRecylerView(movieList);
        }

    }

    private void PutDataIntoRecylerView(List<MovieModelClass> movieList){
        Adaptery adaptery = new Adaptery(this,movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptery);
    }

}