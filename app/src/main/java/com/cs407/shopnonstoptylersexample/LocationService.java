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

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


public class LocationService extends Service {

    private static int NOTIFICATION_ID = 1;
    private static final String FIRST_CHANNEL_ID = "Location Tracking";
    private static final String SECOND_CHANNEL_ID = "Nearby Groceries";

    private LocationManager locationManager;
    private LocationListener locationListener;

    // Places API requires billing info, will use coordinates of groceries in Madison for development
    private static final double coordinates[][] = new double[][]{{43.07276725579338, -89.39003664417623}, {43.07308515174808, -89.3977415422632}, {43.075295211878796, -89.39613221683253}};

    @Override
    public void onCreate() {
        super.onCreate();
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
    }

    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    "Location Tracking Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification createNotification(String channelId, String content) {
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(channelId)
                .setContentText(content)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .build();
        return notification;
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double userLat = location.getLatitude();
                double userLong = location.getLongitude();
                Log.i("INFO", userLat + " " + userLong);
                for (double[] coordinate : coordinates) {
                    double distance = calculateDistance(userLat, userLong, coordinate[0], coordinate[1]);
                    Log.i("INFO", "" + distance);
                    if (distance < 5) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = createNotification(SECOND_CHANNEL_ID, "There is a grocery less than 5 miles away, would you like to check it out?");
                        notificationManager.notify(NOTIFICATION_ID++, notification);
                        break;
                    }
                }
            }
        };

        // Request location updates
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,  500, locationListener);
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