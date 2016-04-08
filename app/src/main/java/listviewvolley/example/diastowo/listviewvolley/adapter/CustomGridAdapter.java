package listviewvolley.example.diastowo.listviewvolley.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import listviewvolley.example.diastowo.listviewvolley.R;
import listviewvolley.example.diastowo.listviewvolley.app.AppController;
import listviewvolley.example.diastowo.listviewvolley.model.Movie;

/**
 * Created by Diastowo on 4/8/2016.
 */
public class CustomGridAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomGridAdapter (Activity activity, List<Movie> movieItems){
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int position) {
        return movieItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.grid_row, null);
        }
        if (imageLoader == null){
            imageLoader = AppController.getInstance().getImageLoader();
        }

        NetworkImageView thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);

        //getting movie data for the row
        Movie m = movieItems.get(position);

        //thumbnail image
        thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        //title
        title.setText(m.getTitle());

        //rating
        rating.setText("Rating: "+String.valueOf(m.getRating()));

/*
        //genre
        String genreString = "";
        for (String str : m.getGenre()){
            genreString += str + ", ";
        }
        genreString = genreString.length() > 0 ? genreString.substring(0,
                genreString.length() - 2) : genreString;
        genre.setText(genreString);

        //release year
        year.setText(String.valueOf(m.getYear()));
*/

        return convertView;
    }
}
