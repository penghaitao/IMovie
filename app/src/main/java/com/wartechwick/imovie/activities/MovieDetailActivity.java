package com.wartechwick.imovie.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.wartechwick.imovie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getIntent().getStringExtra("title"));
                startActivity(intent);
                break;
            case R.id.action_share:
                shareMovie();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareMovie() {
        Intent intent = getIntent();
        if (intent != null) {
            String shareContent = "";
            String tabTitle = intent.getStringExtra("tab");
            String title = intent.getStringExtra("title");
            String overview = intent.getStringExtra("overview");
            if ("UPCOMING".equals(tabTitle)) {
                String releaseDate = intent.getStringExtra("releaseDate");
                shareContent = "*" + title + "*\n\n" + overview + "\n\nRelease Date: " + releaseDate + "\n";
            } else {
                Double rating = intent.getDoubleExtra("rate", 0);
                shareContent = "*" + title + "*\n\n" + overview + "\n\nRating: " + rating + " / 10\n";
            }
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            startActivity(Intent.createChooser(myIntent, "Share with"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }
}
