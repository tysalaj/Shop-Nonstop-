package com.cs407.shopnonstoptylersexample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LocationService extends Service {

    public static boolean IS_SERVICE_RUNNING = false;
    private static int NOTIFICATION_ID = 1;
    private static final String FIRST_CHANNEL_ID = "Location Tracking";
    private static final String SECOND_CHANNEL_ID = "Nearby Groceries";

    private LocationManager locationManager;
    private LocationListener locationListener;

    // Places API requires billing info, will use coordinates of groceries in Madison for development
    // first three are Madison, fourth one is chicago, and fifth one is new york, Dallas Texas, LA California,
    // Seattle, Oconomowoc WI, Waukesha WI
    private static final double[][] coordinates = new double[][]{{43.07276725579338, -89.39003664417623}, {43.07308515174808, -89.3977415422632}, {43.075295211878796, -89.39613221683253}, {41.8758, -87.6295}, {40.7117, -74.0065
    },{32.7784,-96.7963},{34.0543,-118.2411},{47.6070,-122.3335},{43.1058,-88.4774},{43.0124,-88.2281}};

    @Override
    public void onCreate() {
        super.onCreate();
        IS_SERVICE_RUNNING = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start location updates when the service is started
        createNotificationChannel(FIRST_CHANNEL_ID);
        startForeground(NOTIFICATION_ID++, createNotification(FIRST_CHANNEL_ID, "Shop Nonstop is currently tracking your location"));
        createNotificationChannel(SECOND_CHANNEL_ID);
        startLocationUpdates();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        IS_SERVICE_RUNNING = false;
    }

    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification createNotification(String channelId, String content) {
        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle(channelId)
                .setContentText(content)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .build();
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DatabaseReference db = database.getReference();
                DatabaseReference uidRef = db.child("users").child(uid);

                double userLat = location.getLatitude();
                double userLong = location.getLongitude();
                Log.i("INFO", userLat + " " + userLong);

                uidRef.child("distance").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String maxRadius = "";
                        if (snapshot.exists()) {
                            maxRadius = Objects.requireNonNull(snapshot.getValue(String.class)).trim();
                        }
                        for (double[] coordinate : coordinates) {
                            double maxRadiusValue = !maxRadius.isEmpty() ? Double.parseDouble(maxRadius) : 2;
                            double distance = calculateDistance(userLat, userLong, coordinate[0], coordinate[1]);
                            Log.i("INFO", "" + distance);
                            if (distance < maxRadiusValue) {
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                Notification notification = createNotification(SECOND_CHANNEL_ID, "Grocery Store Within " + maxRadiusValue + " miles! Check it out!");
                                notificationManager.notify(NOTIFICATION_ID++, notification);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };

        // Request location updates
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,  500, locationListener);
        }
    }

    private void stopLocationUpdates() {
        // Stop location updates when the service is destroyed
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 3958.8; // Earth radius in miles

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in miles
    }
}