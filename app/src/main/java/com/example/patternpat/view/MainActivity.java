package com.example.patternpat.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.patternpat.R;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_SEND_SMS = 324;
    private NavController navController;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (DrawerLayout) null);
    }

    //Check if we have the SMS permission
    public void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //if we dont have the SMS permission do we have to show the request permission rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                //show rationale
                new AlertDialog.Builder(this)
                        .setTitle("Send SMS Permission")
                        .setMessage("This app requires access to send an sms ")
                        .setPositiveButton("Ask me", (dialog, which) -> {
                            requestSmsPermission();
                        })
                        .setNegativeButton("No", ((dialog, which) -> {
                            notifyDetailFragment(false);
                        }))
                        .show();
            } else {
                //Otherwise we simply request the permission
                requestSmsPermission();
            }
        } else {
            notifyDetailFragment(true);
        }
    }

    private void requestSmsPermission() {
        String[] permissions = {Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this,permissions,PERMISSION_SEND_SMS);
    }


    //Get the result and notify the fragment
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    notifyDetailFragment(true);
                } else {
                    notifyDetailFragment(false);
                }
                break;
            }
        }
    }

    private void notifyDetailFragment(boolean permissionGranted) {
        Fragment activeFragment = fragment.getChildFragmentManager().getPrimaryNavigationFragment();
        if(activeFragment instanceof DetailFragment){
            ((DetailFragment) activeFragment).onPermissionResult(permissionGranted);
        }
    }
}