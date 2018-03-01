package com.example.cse6324.university_bazaar_system;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EntryScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragMan = getFragmentManager();
        Fragment myFrag = FeedFragment.newInstance("", "");
        fragMan.beginTransaction()
                .add(R.id.content_entry, myFrag)
                .commit();
        getSupportActionBar().setTitle(R.string.home);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Get current logged in user and display on navigation drawer
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        final TextView textView  = (TextView) headerView.findViewById(R.id.textView);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(auth.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);
                textView.setText(currentUser.getName());
            }
        });

    }

    public void setActionBarTitle(int title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.entry_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentManager fragMan = getFragmentManager();
            Fragment myFrag = FeedFragment.newInstance("", "");
            FragmentTransaction transaction = fragMan.beginTransaction();
            transaction.replace(R.id.content_entry, myFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_clubs_organizations) {
            FragmentManager fragMan = getFragmentManager();
            Fragment myFrag = ClubFragment.newInstance("", "");
            FragmentTransaction transaction = fragMan.beginTransaction();
            transaction.replace(R.id.content_entry, myFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_messages) {
            FragmentManager fragMan = getFragmentManager();
            Fragment myFrag = MessagesFragment.newInstance("", "");
            FragmentTransaction transaction = fragMan.beginTransaction();
            transaction.replace(R.id.content_entry, myFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_marketplace) {
            FragmentManager fragMan = getFragmentManager();
            Fragment myFrag = MarketplaceFragment.newInstance("", "");
            FragmentTransaction transaction = fragMan.beginTransaction();
            transaction.replace(R.id.content_entry, myFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            // this listener will be called when there is change in firebase user session

            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        // user auth state is changed - user is null
                        // launch login activity
                        startActivity(new Intent(EntryScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };
            auth.addAuthStateListener(authListener);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
