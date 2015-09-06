package com.hnttechs.www.theladies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleItemView_tarot extends ActionBarActivity {
	// Declare Variables
	String title;
	String body;
	String createddate;
    String path;
    String condition;
    Integer position;

    static TextView txttitle;
    static TextView txtbody;
    static TextView txtcreateddate;
    private static final String json_tarot = "/sdcard/8Eggs/egg_tarot.json";
    private static final String json_events = "/sdcard/8Eggs/egg_events.json";
    ImageLoader_cartoon imageLoader = new ImageLoader_cartoon(this);

    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static ScrollView singleitemview_layout;


    String ads_image;
    static ArrayList<HashMap<String, String>> arraylist_ads;

    private static final String json_ads = "/sdcard/8Eggs/ads.json";
    static ImageView img_ads;
    ArrayList<HashMap<String, String>> data_ads;
    HashMap<String, String> resultp_ads = new HashMap<String, String>();
    static String ADS_IMAGE = "ads_image";
    JSONObject jsonobject_ads;
    JSONObject ads;
    File file_ads;
    static long length;
    private int currentIndex;
    private int startIndex;


    Handler handler; // declared before onCreate
    Runnable myRunnable;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singleitemview_tarot);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		title = i.getStringExtra("title");
		body = i.getStringExtra("body");
        condition = i.getStringExtra("condition");
        path = i.getStringExtra("path");
		createddate = i.getStringExtra("createddate");
        position = i.getIntExtra("position", 0);
		txttitle = (TextView) findViewById(R.id.title);
		txtbody = (TextView) findViewById(R.id.body);
		txtcreateddate = (TextView) findViewById(R.id.createddate);
        img_ads = (ImageView) findViewById(R.id.ads);

		txttitle.setText(title);
		txtbody.setText(body);
		txtcreateddate.setText(createddate);

        singleitemview_layout = (ScrollView) findViewById(R.id.singleitemview_tarot_layout);
        data = ListViewMain.arraylist;
        resultp = data.get(position);



        startIndex = 0;

        file_ads = new File(json_ads);

        length = file_ads.length();
        if (file_ads.exists() && length != 0 ) {
            read_ads();
            nextImage();


        }



        singleitemview_layout.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeRight() {
                if (position >= 0) {
                    if(position == 0) {
                        resultp = data.get(position);

                        txttitle.setText(resultp.get(ListViewMain.TITLE));
                        txtbody.setText(resultp.get(ListViewMain.BODY));
                        txtcreateddate.setText(resultp.get(ListViewMain.CREATEDDATE));
                        condition = resultp.get(ListViewMain.CONDITION);
                        path = resultp.get(ListViewMain.PATH);
                        if (ListViewMain.btn_id.equals("tarot")) {
                            change_read_status(json_tarot, "Tarot Magic");
                        } else if (ListViewMain.btn_id.equals("events")) {
                            change_read_status(json_events, "Events");
                        }
                        invalidateOptionsMenu();
                    } else {
                        resultp = data.get(position - 1);
                        position = position - 1;


                        txttitle.setText(resultp.get(ListViewMain.TITLE));
                        txtbody.setText(resultp.get(ListViewMain.BODY));
                        txtcreateddate.setText(resultp.get(ListViewMain.CREATEDDATE));
                        condition = resultp.get(ListViewMain.CONDITION);
                        path = resultp.get(ListViewMain.PATH);
                        if (ListViewMain.btn_id.equals("tarot")) {
                            change_read_status(json_tarot, "Tarot Magic");
                        } else if (ListViewMain.btn_id.equals("events")) {
                            change_read_status(json_events, "Events");
                        }
                        invalidateOptionsMenu();
                    }

                }
            }

            public void onSwipeLeft() {
                if (position <= data.size()) {
                    if(position == data.size()-1) {
                        resultp = data.get(position);

                        txttitle.setText(resultp.get(ListViewMain.TITLE));
                        txtbody.setText(resultp.get(ListViewMain.BODY));
                        txtcreateddate.setText(resultp.get(ListViewMain.CREATEDDATE));
                        path = resultp.get(ListViewMain.PATH);
                        condition = resultp.get(ListViewMain.CONDITION);
                        if (ListViewMain.btn_id.equals("tarot")) {
                            change_read_status(json_tarot, "Tarot Magic");
                        } else if (ListViewMain.btn_id.equals("events")) {
                            change_read_status(json_events, "Events");
                        }
                        invalidateOptionsMenu();
                    } else {
                        resultp = data.get(position + 1);
                        position = position + 1;

                        txttitle.setText(resultp.get(ListViewMain.TITLE));
                        txtbody.setText(resultp.get(ListViewMain.BODY));
                        txtcreateddate.setText(resultp.get(ListViewMain.CREATEDDATE));
                        path = resultp.get(ListViewMain.PATH);
                        condition = resultp.get(ListViewMain.CONDITION);
                        if (ListViewMain.btn_id.equals("tarot")) {
                            change_read_status(json_tarot, "Tarot Magic");
                        } else if (ListViewMain.btn_id.equals("events")) {
                            change_read_status(json_events, "Events");
                        }
                        invalidateOptionsMenu();
                    }

                }
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


    }



    public void change_read_status(String fileName, String jsonArrayName) {

        try {
            InputStream jsonStream = new FileInputStream(fileName);
            JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
            JSONArray jsonreadArray = jsonObject.getJSONArray(jsonArrayName);

            for (int i = 0; i < jsonreadArray.length(); i++) {
                JSONObject latestnews = jsonreadArray.getJSONObject(i);
                if (latestnews.getString("title").equals(resultp.get(ListViewMain.TITLE))) {

                    JSONArray jsonwriteArray = jsonObject.getJSONArray(jsonArrayName);
                    for (int j = 0; j < jsonwriteArray.length(); j++) {
                        JSONObject writenews = jsonreadArray.getJSONObject(j);
                        if (writenews.getString("title").equals(resultp.get(ListViewMain.TITLE))) {
                            if (writenews.getString("condition").equals("0") || writenews.getString("condition").equals("0\n")){
                                writenews.put("condition", "1");
                            } else {
                                writenews.put("condition", writenews.getString("condition"));
                            }
                        }
                    }
                }
            }

            FileWriter jsonFileWriter = new FileWriter(fileName);
            jsonFileWriter.write(jsonObject.toString());
            jsonFileWriter.flush();
            jsonFileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

    }


    private void shareIt() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing 8EGGS news to facebook.");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, path);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }




    private void star() {
        if (ListViewMain.btn_id.equals("tarot")) {
            CheckJSON_method(json_tarot, "Tarot Magic");
        } else if (ListViewMain.btn_id.equals("events")) {
            CheckJSON_method(json_events, "Events");
        }
    }

    public void CheckJSON_method(String fileName, String jsonArrayName) {

        try {
            InputStream jsonStream = new FileInputStream(fileName);
            JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
            JSONArray jsonreadArray = jsonObject.getJSONArray(jsonArrayName);

            for (int i = 0; i < jsonreadArray.length(); i++) {
                JSONObject latestnews = jsonreadArray.getJSONObject(i);
                if (latestnews.getString("title").equals(title)) {

                    JSONArray jsonwriteArray = jsonObject.getJSONArray(jsonArrayName);
                    for (int j = 0; j < jsonwriteArray.length(); j++) {
                        JSONObject writenews = jsonreadArray.getJSONObject(j);
                        if (writenews.getString("title").equals(title)) {
                            writenews.put("condition", "3");
                        }
                    }
                }
            }

            FileWriter jsonFileWriter = new FileWriter(fileName);
            jsonFileWriter.write(jsonObject.toString());
            jsonFileWriter.flush();
            jsonFileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean check_star(String condition) {
        if (condition.equals("3")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            shareIt();
        } else if (id == R.id.star) {
            star();
            item.setIcon(R.drawable.star_clicked);
        } else {
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menu_star = menu.findItem(R.id.star);
        if(check_star(condition)) {
            menu_star.setIcon(R.drawable.star_clicked);
        } else {
            menu_star.setIcon(R.drawable.star);
        }
        return super.onPrepareOptionsMenu(menu);
    }



    private ArrayList read_ads() {

        arraylist_ads = new ArrayList<HashMap<String, String>>();

        try {
            InputStream jsonStream = new FileInputStream(json_ads);
            jsonobject_ads = new JSONObject(InputStreamToString(jsonStream));
            JSONArray adsarray = jsonobject_ads.getJSONArray("Mobile Ad");

            for (int i = 0; i < adsarray.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                ads = adsarray.getJSONObject(i);

                map.put("ads_image", ads.getString("field_image"));

                arraylist_ads.add(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return arraylist_ads;
    }


    public static String InputStreamToString(InputStream is) {

        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return total.toString();
    }




    public void nextImage(){

        data_ads = arraylist_ads;
        resultp_ads = data_ads.get(currentIndex);
        ads_image = resultp_ads.get(this.ADS_IMAGE);
        imageLoader.DisplayImage(ads_image, img_ads);


        currentIndex = currentIndex+1;

        handler=  new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                if(currentIndex>data_ads.size()-1){
                    currentIndex--;
                    previousImage();
                }else{
                    nextImage();
                }

            }
        };
        handler.postDelayed(myRunnable,1000);

    }
    public void previousImage(){

        data_ads = arraylist_ads;
        resultp_ads = data_ads.get(currentIndex);
        ads_image = resultp_ads.get(this.ADS_IMAGE);
        imageLoader.DisplayImage(ads_image, img_ads);

        currentIndex--;

        handler=  new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                if(currentIndex<startIndex){
                    currentIndex++;
                    nextImage();
                }else{
                    previousImage(); // here 1000(1 second) interval to change from current  to previous image
                }
            }
        };
        handler.postDelayed(myRunnable,1000);

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(myRunnable);
        this.finish();
        return;
    }
}