package com.sankar.popularmovies;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sankar.popularmovies.database.MovieColumns;
import com.sankar.popularmovies.database.MovieProvider;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsFragment extends Fragment implements VideoRecyclerAdapter.ClickListener{
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    ArrayList<Review> reviewArrayList = new ArrayList<Review>();
    ArrayList<Trailer> trailerArrayList = new ArrayList<Trailer>();

    RecyclerView reviewRecyclerView, videoRecyclerView;
    ReviewRecyclerAdapter reviewRecyclerAdapter;
    VideoRecyclerAdapter videoRecyclerAdapter;

    ImageButton imageButton;
    boolean on = true;
    Movie movie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment newInstance(int position) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        else {
            mCurrentPosition = getActivity().getIntent().getIntExtra(DetailActivity.ARG_POSITION, -1);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            updateMovieDetailsView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            updateMovieDetailsView(mCurrentPosition);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void sendShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String id = trailerArrayList.isEmpty()?"":trailerArrayList.get(0).getKey();
        shareIntent.putExtra(Intent.EXTRA_TEXT,"http://www.youtube.com/watch?v=" + id);
        startActivity(Intent.createChooser(shareIntent, "Share video using"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_share)
        {
            sendShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovieDetailsView(int position) {

        movie = MovieService.getInstance().getMovie(position);

        new FetchReviewsTask().execute(String.valueOf(movie.getId()));
        new FetchTrailersTask().execute(String.valueOf(movie.getId()));

        ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView1);
        TextView txtMovieDesc = (TextView) getActivity().findViewById(R.id.txtMovieDesc);
        TextView textViewVoteAverage = (TextView) getActivity().findViewById(R.id.textViewVote2);
        TextView textViewReleaseDate = (TextView) getActivity().findViewById(R.id.textViewDate2);

        imageButton = (ImageButton) getActivity().findViewById(R.id.favorite);
        imageButton.setImageResource(android.R.drawable.btn_star_big_off);
        on = true;
        String selection = MovieColumns.MOVIE_ID+"="+movie.getId();
        Cursor cursor = null;
        try{
            cursor = getContext().getContentResolver().query(MovieProvider.Movies.CONTENT_URI, null, selection, null, null);
            if(cursor!=null && cursor.moveToFirst())
            {
                imageButton.setImageResource(android.R.drawable.btn_star_big_on);
                on = false;
            }
        }
        catch (Exception e)
        {
            Log.e("error",e.getLocalizedMessage());
        }
        finally {
            if(cursor!=null)
            {
                cursor.close();
            }
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleStar(v);
            }
        });

        Picasso.with(getContext()).load(movie.getImage_path()).placeholder(R.drawable.placeholder).into(imageView);
        imageView.getLayoutParams().height = 500; // OR
        imageView.getLayoutParams().width = 500;
        imageView.setContentDescription(movie.getTitle());
        textViewVoteAverage.setText(Double.toString(movie.getVote_average())+"/10");
        textViewReleaseDate.setText(movie.getRelease_date());
        txtMovieDesc.setText(movie.getOverview());
        getActivity().setTitle(movie.getTitle());

        reviewRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(reviewArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        reviewRecyclerView.setLayoutManager(mLayoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(reviewRecyclerAdapter);

        videoRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_video);
        videoRecyclerAdapter = new VideoRecyclerAdapter(getContext(), trailerArrayList, this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
        videoRecyclerView.setLayoutManager(mLayoutManager2);
        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclerView.setAdapter(videoRecyclerAdapter);

        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    public void onToggleStar(View view){

        if (on) {
            //Log.i("info", "Button1 is on!");
            imageButton.setImageResource(android.R.drawable.btn_star_big_on);
            ContentValues cv = new ContentValues();

            cv.put(MovieColumns.MOVIE_ID, movie.getId());
            cv.put(MovieColumns.TITLE, movie.getTitle());
            cv.put(MovieColumns.IMAGE_PATH, movie.getImage_path());
            cv.put(MovieColumns.POPULARITY, movie.getPopularity());
            cv.put(MovieColumns.OVERVIEW, movie.getOverview());
            cv.put(MovieColumns.VOTE_COUNT, movie.getVote_count());
            cv.put(MovieColumns.VOTE_AVERAGE, movie.getVote_average());
            cv.put(MovieColumns.RELEASE_DATE, movie.getRelease_date());

            getContext().getContentResolver().insert(MovieProvider.Movies.CONTENT_URI,
                    cv);

            on = false;
        } else {
            //Log.i("info", "Button1 is off!");
            imageButton.setImageResource(android.R.drawable.btn_star_big_off);
            getContext().getContentResolver().delete(MovieProvider.Movies.withId(movie.getId()), null, null);
            on = true;
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        Trailer trailer = trailerArrayList.get(position);
        String id = trailer!=null?trailer.getKey():"";
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {

        @Override
        protected ArrayList doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String reviewJsonStr = "";
            ArrayList<Review> reviewList = new ArrayList<>();

            try {
                String baseUrl = "http://api.themoviedb.org/3/movie/";
                StringBuilder urlBuilder = new StringBuilder(baseUrl);
                urlBuilder.append(params[0]).append("/reviews").append("?")
                        .append("api_key").append("=").append(MovieConstants.API_KEY);

                //Log.v("Review URL: ", urlBuilder.toString());

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
                reviewJsonStr = buffer.toString();
                //Log.v("reviewJsonStr: ", reviewJsonStr);
                if (!reviewJsonStr.isEmpty()) {
                    reviewList = new ReviewParser().parseJsonString(reviewJsonStr);
                    //Log.v("ReviewList: ",reviewList.toString());
                }
            } catch (IOException e) {
                Log.e("FetchReviewsTask", "error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("FetchReviewsTask", "error", e);
                    }
                }
            }

            return reviewList;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> listReview) {
            reviewArrayList.clear();
            if(listReview!=null) {
                reviewArrayList.addAll(listReview);
            }
            reviewRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        @Override
        protected ArrayList doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String reviewJsonStr = "";
            ArrayList<Trailer> trailerList = new ArrayList<>();

            try {
                String baseUrl = "http://api.themoviedb.org/3/movie/";
                StringBuilder urlBuilder = new StringBuilder(baseUrl);
                urlBuilder.append(params[0]).append("/videos").append("?")
                        .append("api_key").append("=").append(MovieConstants.API_KEY);

                //Log.v("Videos URL: ", urlBuilder.toString());

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
                reviewJsonStr = buffer.toString();
                //Log.v("reviewJsonStr: ", reviewJsonStr);
                if (!reviewJsonStr.isEmpty()) {
                    trailerList = new VideoParser().parseJsonString(reviewJsonStr);
                    //Log.v("VideoList: ",trailerList.toString());
                }
            } catch (IOException e) {
                Log.e("FetchVideosTask", "error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("FetchVideosTask", "error", e);
                    }
                }
            }

            return trailerList;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> listTrailer) {
            trailerArrayList.clear();
            if(listTrailer!=null) {
                trailerArrayList.addAll(listTrailer);
            }
            videoRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
