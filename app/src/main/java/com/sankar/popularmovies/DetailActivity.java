package com.sankar.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    public static final String ARG_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(findViewById(R.id.fragment_detail_container)!=null) {
            if (savedInstanceState != null) {
                return;
            }

            int position = getIntent().getIntExtra(ARG_POSITION, -1);
            MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(position);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_container, movieDetailsFragment).commit();

        }
    }
}
