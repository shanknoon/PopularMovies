package com.sankar.popularmovies;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sankar.popularmovies.database.MovieProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PopularMoviesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageAdapter mImageAdapter;
    ArrayList<Movie> movieList = MovieService.getInstance().getListOfMovies();
    String sortBy = MovieConstants.POPULARITY;

    OnMovieSelectedListener mCallback;

    public interface OnMovieSelectedListener {
        public void onMovieSelected(int position);
    }

    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularMoviesFragment newInstance(String param1, String param2) {
        PopularMoviesFragment fragment = new PopularMoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.grid_main, container, false);

        if (savedInstanceState == null && movieList.isEmpty()) {
            new FetchMoviesTask().execute(MovieConstants.POPULARITY);
        }

        mImageAdapter = new ImageAdapter(getContext(), MovieService.getInstance().getListOfMovies());
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onMovieSelected(position);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sortByPopularity)
        {
            sortBy = MovieConstants.POPULARITY;
            new FetchMoviesTask().execute(sortBy);
            return true;
        }else if(id == R.id.sortByRated)
        {
            sortBy = MovieConstants.HIGHEST_RATED;
            new FetchMoviesTask().execute(sortBy);
            return true;
        }
        else if(id == R.id.favorites)
        {
            getFavoriteMovie();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFavoriteMovie() {
        MovieService movieService = MovieService.getInstance();
        movieService.removeAllMovies();

        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(MovieProvider.Movies.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++,cursor.moveToNext()) {
                    Movie movie = new Movie();
                    movie.setId(cursor.getLong(1));
                    movie.setTitle(cursor.getString(2));
                    movie.setImage_path(cursor.getString(3));
                    movie.setPopularity(cursor.getDouble(4));
                    movie.setVote_count(cursor.getLong(5));
                    movie.setVote_average(cursor.getDouble(6));
                    movie.setOverview(cursor.getString(7));
                    movie.setRelease_date(cursor.getString(8));
                    movieService.addMovie(movie);
                }
            }
        }
        catch (Exception e)
        {
            Log.e("error", e.getLocalizedMessage());
        }
        finally {
            if (cursor!=null)
            {
                cursor.close();
            }
        }
        mImageAdapter.notifyDataSetChanged();
        if (getFragmentManager().findFragmentById(R.id.details_fragment) != null) {
            if (movieService.getListOfMovies() != null && movieService.getListOfMovies().size() > 0) {
                mCallback.onMovieSelected(0);
            }
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = "";

            try {
                String baseUrl = "http://api.themoviedb.org/3/discover/movie";
                StringBuilder urlBuilder = new StringBuilder(baseUrl);
                urlBuilder.append("?").append(MovieConstants.SORT_BY).append("=").append(params[0])
                        .append(".").append(MovieConstants.DESC).append("&")
                        .append("api_key").append("=").append(MovieConstants.API_KEY);

                URL url = new URL(urlBuilder.toString());//"http://api.themoviedb.org/3/discover/movie?"+MovieConstants.SORT_BY+"="+params[0]+"."+MovieConstants.DESC+"&api_key="+MovieConstants.API_KEY);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer = buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
                //Log.v("movieJsonStr: ", movieJsonStr);
                if (!movieJsonStr.isEmpty()) {
                    movieList = new ImageParser().parseJsonString(movieJsonStr);
                    return movieList;
                }
            } catch (IOException e) {
                Log.e("MovieFetchTask", "error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("FetchMoviesTask", "error", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList listMovie)
        {
            mImageAdapter.notifyDataSetChanged();
            if (getFragmentManager().findFragmentById(R.id.details_fragment) != null) {
                if (listMovie != null && listMovie.size() > 0) {
                    mCallback.onMovieSelected(0);
                }
            }
        }

    }
}
