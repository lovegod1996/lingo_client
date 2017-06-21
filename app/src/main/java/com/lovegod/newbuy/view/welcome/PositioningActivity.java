package com.lovegod.newbuy.view.welcome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.List;

import static com.lovegod.newbuy.R.id.city_name;
import static com.lovegod.newbuy.R.id.content;

/**
 * *****************************************
 * Created by thinking on 2017/6/10.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class PositioningActivity {
    public static String cityName;//城市名
    private static Geocoder geocoder;

    public static void getCNBylocation(Context context) {
        geocoder = new Geocoder(context);
        String serviceName = Context.LOCATION_SERVICE;
        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(serviceName);

        //provider的类型
        String provider = LocationManager.NETWORK_PROVIDER;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        String queryed_name = updateWithNewLocation(location);
        if ((queryed_name != null) && (0 != queryed_name.length())) {
           // city_name.setText(queryed_name);
            cityName=queryed_name;
        }
        locationManager.requestLocationUpdates(provider,3000,50,locationListener);
        locationManager.removeUpdates(locationListener);



    }
    private final static LocationListener locationListener = new LocationListener() {
        String tempCityName;
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled (String provider){
            tempCityName = updateWithNewLocation(null);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {
               // city_name.setText(tempCityName);
                cityName=tempCityName;
            }
        }
        @Override
        public void onLocationChanged (Location location){
            tempCityName = updateWithNewLocation(location);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {
               // city_name.setText(tempCityName);
                cityName=tempCityName;
            }
        }
    };


    private static String updateWithNewLocation(Location location) {
        String mcityName = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
           /// city_name.setText("错了");
        }
        try {
            addList = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mcityName += add.getLocality();
            }
        }
        if (mcityName.length() != 0) {
            return mcityName.substring(0, (mcityName.length() - 1));
        } else {
            return mcityName;
        }
    }


}
