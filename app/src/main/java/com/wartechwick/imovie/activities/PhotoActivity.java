package com.wartechwick.imovie.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.wartechwick.imovie.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class PhotoActivity extends AppCompatActivity {

    @BindView(R.id.iv_photo)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        String backdrop = getIntent().getStringExtra("backdrop");
        Glide.with(this).load(backdrop).into(photoView);
    }

}
