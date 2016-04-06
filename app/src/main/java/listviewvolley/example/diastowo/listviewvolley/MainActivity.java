package listviewvolley.example.diastowo.listviewvolley;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import listviewvolley.example.diastowo.listviewvolley.adapter.CustomListAdapter;
import listviewvolley.example.diastowo.listviewvolley.app.AppController;
import listviewvolley.example.diastowo.listviewvolley.model.Movie;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //movies json url
    private static final String  url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog progressDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //create volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d(TAG, jsonArray.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(obj.getString("title"));
                        movie.setThumbnailUrl(obj.getString("image"));
                        movie.setRating(((Number) obj.get("rating"))
                                .doubleValue());
                        movie.setYear(obj.getInt("releaseYear"));

                        // Genre is json array
                        JSONArray genreArry = obj.getJSONArray("genre");
                        ArrayList<String> genre = new ArrayList<String>();
                        for (int j = 0; j < genreArry.length(); j++) {
                            genre.add((String) genreArry.get(j));
                        }
                        movie.setGenre(genre);

                        // adding movie to movies array
                        movieList.add(movie);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "Item Clicked at: "+ movieList.get(position).getTitle()
                        + "Rating : " + movieList.get(position).getRating()
                        + "Year : " + movieList.get(position).getYear());
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
