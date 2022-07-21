package com.example.infinity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /*NAVIGATION INTERFACES*/
    private BottomNavigationView navigationView;
    private NavController navController;


    /*firebase*/
    /*private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting Main_Activity");

        initialize();
        setupBottomNavigation(navigationView , navController);


    }

    private void initialize(){

        navController = (NavController) Navigation.findNavController(this ,R.id.root_nav_host_fragment);
        navigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
    }



    /*this method will setup bottom navigation*/
    private void setupBottomNavigation( final BottomNavigationView bottomNavigationView , NavController navController){
        Log.d(TAG, "setupBottomNavigation: method starting");
        bottomNavigationView.setItemIconTintList(null);

        NavigationUI.setupWithNavController(bottomNavigationView , navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {


                if(destination.getId() == R.id.home_dest){

                    bottomNavigationView.setVisibility(View.VISIBLE);
                }else if (destination.getId() == R.id.downloaded_dest){

                    bottomNavigationView.setVisibility(View.VISIBLE);

                }else if (destination.getId() == R.id.profile_dest){
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }else {
                    bottomNavigationView.setVisibility(View.GONE);
                }

            }
        });
    }




}