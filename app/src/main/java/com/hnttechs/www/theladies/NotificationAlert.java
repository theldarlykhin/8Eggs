package com.hnttechs.www.theladies;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 6/10/15.
 */

public class NotificationAlert extends Service {

    private static final String TAG = NotificationAlert.class.getSimpleName();
    static Integer count = 0;
    static Integer readcount = 0;
    Bitmap icon;
    private static final String json_latest_news = "/sdcard/8Eggs/egg_latest_news.json";
    org.json.JSONObject jsonobject;
    ArrayList<HashMap<String, String>> arraylist;

    File file;
    static JSONObject readnews;
    static String title;






    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (isInternetOn() == true) {
            file = new File(json_latest_news);
            long length = file.length();
            if (file.exists() && length != 0 ) {
                new DownloadJSON().execute();


            }
        }
        return START_STICKY;
    }


    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.egg_logo);
            if (isInternetOn() == true) {

                jsonobject = JSONfunctions.getJSONfromURL("http://www.theladiesnewsjournal.com/latest-news.txt");
                try {
                    InputStream jsonStream = new FileInputStream(json_latest_news);
                    JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
                    JSONArray jsonreadArray = jsonObject.getJSONArray("Latest News");
                    FileWriter jsonFileWriter = new FileWriter(json_latest_news);

                    jsonFileWriter.write(jsonobject.toString());
                    jsonFileWriter.flush();
                    jsonFileWriter.close();

                    JSONArray jsondownArray = jsonobject.getJSONArray("Latest News");
                    for (int i = 0; i < jsondownArray.length(); i++) {

                        readnews = jsonreadArray.getJSONObject(1);
                        title = readnews.getString("title");

                        JSONObject downnews = jsondownArray.getJSONObject(i);
                        if (downnews.getString("title").equals(title)) {
                            break;
                        } else {
                            count = count + i;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (count > 0) {

                PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 0, new Intent(getBaseContext(), MainActivity.class), 0);
                Resources r = getResources();
                Notification notification = new NotificationCompat.Builder(getBaseContext())
                        .setTicker("8EGGS")
                        .setSmallIcon(R.drawable.egg_logo)
                        .setContentTitle("8EGGS")
                        .setContentText("သတင္းအသစ္ " + count.toString() + " ပုဒ္ရွိပါသည္။")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification);

                count = 0;
            }

        }
    }

    public final boolean isInternetOn() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}
