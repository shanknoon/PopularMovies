package com.sankar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements PopularMoviesFragment.OnMovieSelectedListener{

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies);

        if(findViewById(R.id.fragment_container)!=null)
        {
            if(savedInstanceState!=null)
            {
                return;
            }

            PopularMoviesFragment popularMoviesFragment = new PopularMoviesFragment();
            popularMoviesFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, popularMoviesFragment).commit();
        }

    }

    @Override
    public void onMovieSelected(int position) {

        MovieDetailsFragment detailsFrag = (MovieDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.details_fragment);

        if (detailsFrag != null) {
            detailsFrag.updateMovieDetailsView(position);

        } else {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.ARG_POSITION, position);
            startActivity(intent);
        }

    }
}
