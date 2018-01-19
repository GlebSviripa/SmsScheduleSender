package co.tula.sendsmsapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

/*
 * Created by Gleb
 * TulaCo 
 * 1/19/2018
 */

public class SmsService extends Service {

    public static final int SERVICE_ID = 100;

    public static void schedule(Context context, String phone) {
        Intent intent = new Intent(context, SmsService.class);
        intent.putExtra("phone", phone);
        PendingIntent pendingIntent = PendingIntent.getService(context, SmsService.SERVICE_ID, intent, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        alarm.cancel(pendingIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60*1000, pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("SMS", "onStartCommand");
        LocationHelper.getCurrentLocation(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                SmsHelper.sendSms(SmsService.this, intent.getStringExtra("phone"), "location: " + location.getLatitude() + "; " + location.getLongitude());
                stopSelf();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

}
