package com.example.ian.paypalpayment.Config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Config {
    public static final String PAYPAL_CLIENT_ID = "ATnjF38xjmWkFHzUu95Pl8pYehPWLzFnh49EMXTEPcbCcVq1zmXSwJacgQ3-Fk4hm1b08TzlA6ZTLnPV";

    public static final String LOGIN = "http://192.168.1.224/payment/public/api/login";

    public static final String BASE_URL = "http://192.168.1.224/payment/public/api/";

    public static final String SOCKET_SERVER = "http://192.168.1.224:3000";

    //single ton
    private static Config instance = new Config();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static Config getInstance(Context cont)
    {
        context = cont.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

}
