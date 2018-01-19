package co.tula.sendsmsapp;
/*
 * Created by Gleb
 * TulaCo 
 * 1/19/2018
 */

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    private Button scheduleButton;
    private Button sendImmediatelyButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleButton = findViewById(R.id.scheduleButton);
        sendImmediatelyButton = findViewById(R.id.sendImmediatelyButton);

        final RxPermissions rxPermissions = new RxPermissions(this);
        if (!rxPermissions.isGranted(Manifest.permission.SEND_SMS))
            rxPermissions.request(Manifest.permission.SEND_SMS)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (!granted)
                                showErrorPermission();
                        }
                    });
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (!granted)
                                showErrorPermission();
                        }
                    });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rxPermissions.isGranted(Manifest.permission.SEND_SMS) || !rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    showErrorPermission();
                    return;
                }
                SmsHelper.scheduleSms(MainActivity.this, "+380930626198");
            }
        });

        sendImmediatelyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rxPermissions.isGranted(Manifest.permission.SEND_SMS) || !rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    showErrorPermission();
                    return;
                }
                LocationHelper.getCurrentLocation(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        SmsHelper.sendSms(MainActivity.this, "+380930626198", "location: " + location.getLatitude() + "; " + location.getLongitude());
                    }
                });
            }
        });


    }

    private void showErrorPermission() {
        Snackbar.make(scheduleButton, "You need permissions for sending sms and location", Snackbar.LENGTH_LONG)
                .show();
    }

}
