package com.fj.naufalprakoso.dicodingkamusmade;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fj.naufalprakoso.dicodingkamusmade.database.KamusDataHelper;
import com.fj.naufalprakoso.dicodingkamusmade.model.Kamus;
import com.fj.naufalprakoso.dicodingkamusmade.preference.AppPreference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EnglishFragment.OnFragmentInteractionListener,
        BahasaFragment.OnFragmentInteractionListener {

    private SimpleProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Jika bukan petama kalo buka aplikasi, maka langsung add fragment, Jika baru pertama, add fragment ketika data
        // Berhasil disimpan
        if (!new AppPreference(this).getFirstRun()){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, EnglishFragment.newInstance());
            transaction.commit();
        }

        setTitle(R.string.english_indonesia);
        progressDialog = new SimpleProgressDialog(this);
        new LoadData().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = null;
        Fragment fragment = null;
        if (id == R.id.english_indo) {
            fragment = EnglishFragment.newInstance();
            title = getString(R.string.english_indonesia);
            // Handle the camera action
        } else if (id == R.id.indo_english) {
            fragment = BahasaFragment.newInstance();
            title = getString(R.string.indonesia_english);
        }

        if (fragment != null) {
            setTitle(title);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void, Integer, Void> {
        final String TAG = LoadData.class.getSimpleName();
        KamusDataHelper kamusDataHelper;
        AppPreference appPreference;

        @Override
        protected void onPreExecute() {
            kamusDataHelper = new KamusDataHelper(MainActivity.this);
            appPreference = new AppPreference(MainActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = appPreference.getFirstRun();
            if (firstRun) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.show();
                        }
                    }
                });

                ArrayList<Kamus> kamusIndo = preLoadRaw(true);
                ArrayList<Kamus> kamusEnglish = preLoadRaw(false);

                kamusDataHelper.open();
                kamusDataHelper.beginTransaction();
                try {
                    for (Kamus model : kamusIndo) {
                        kamusDataHelper.insertTransactionBahasa(model);
                    }

                    for (Kamus model : kamusEnglish) {
                        kamusDataHelper.insertTransactionEnglish(model);
                    }

                    kamusDataHelper.setTransactionSuccess();
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground: Exception");
                }

                kamusDataHelper.endTransaction();
                kamusDataHelper.close();
                appPreference.setFirstRun(false);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, EnglishFragment.newInstance());
            transaction.commit();
        }
    }

    public ArrayList<Kamus> preLoadRaw(boolean indo) {
        ArrayList<Kamus> kamusArrayList = new ArrayList<>();
        String line;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict;
            if (indo) {
                raw_dict = res.openRawResource(R.raw.indonesia_english);
            } else {
                raw_dict = res.openRawResource(R.raw.english_indonesia);
            }

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            do {
                line = reader.readLine();
                if (line != null) {
                    String[] split = line.split("\t");

                    Kamus kamus = new Kamus();
                    kamus.setNama(split[0]);
                    kamus.setKeterangan(split[1]);

                    kamusArrayList.add(kamus);
                }
            } while (line != null);

            Log.e("SIZE LIST ", "" + kamusArrayList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kamusArrayList;
    }
}
