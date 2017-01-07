package com.wartechwick.imovie.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.wartechwick.imovie.BuildConfig;
import com.wartechwick.imovie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.logo)
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(" ");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/canaro_extra_bold.otf");
        logo.setTypeface(typeface);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public static class AboutActivityFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.about_preference_fragment);

            Preference version = findPreference("Version");
            version.setSummary(BuildConfig.VERSION_NAME);

            Preference licence = findPreference("Licence");
            licence.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), LicenceActivity.class));
                    return true;
                }
            });

            Preference feedback = findPreference("feedback");
            feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    try{
                        Uri uri = Uri.parse(getActivity().getString(R.string.sendto));
                        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                        intent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.mail_topic));
                        getActivity().startActivity(intent);
                    }catch (android.content.ActivityNotFoundException ex){
                    }
                    return true;
                }
            });

            Preference rate = findPreference("rate");
            rate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    } catch (android.content.ActivityNotFoundException ex){
                    }
                    return true;
                }
            });

            Preference share = findPreference("share");
            share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String app_share_details = "https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
                    if (!(app_share_details.equals(null))) {
                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        myIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome movie app.\n" + "*MovieZ*\n" + app_share_details);
                        startActivity(Intent.createChooser(myIntent, "Share with"));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
