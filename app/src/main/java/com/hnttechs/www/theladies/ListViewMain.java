package com.hnttechs.www.theladies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
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

public class ListViewMain extends ActionBarActivity {

    /* Variable Declaration */
    org.json.JSONObject jsonobject;
    JSONArray jsonArray;
    ListView listview;
    ListViewAdapter adapter;
    ListViewAdapter_bigphoto adapter_bigphoto;
    ListViewAdapter_tarot adapter_tarot;
    static ArrayList<HashMap<String, String>> arraylist;


    static String TITLE = "title";
    static String BODY_SMALL = "body_small";
    static String BODY = "body";
    static String CREATEDDATE = "createddate";
    static String FIELD_IMAGE = "field_image";
    static String PATH = "path";
    static String CONDITION = "condition";
    private static final String json_latest_news = "/sdcard/8Eggs/egg_latest_news.json";
    private static final String json_news = "/sdcard/8Eggs/egg_news.json";
    private static final String json_world_news = "/sdcard/8Eggs/egg_world_news.json";
    private static final String json_election_news = "/sdcard/8Eggs/egg_election_news.json";
    private static final String json_cartoon = "/sdcard/8Eggs/egg_cartoon.json";
    private static final String json_tarot = "/sdcard/8Eggs/egg_tarot.json";
    private static final String json_article = "/sdcard/8Eggs/egg_article.json";
    private static final String json_events = "/sdcard/8Eggs/egg_events.json";
    private SwipeRefreshLayout swipeContainer;
    JSONObject news;
    JSONObject jsonObject;
    static int condition_refresh;
    static int read;
    static String btn_id;
    File file;
    File eggDirectory;
    FileWriter jsonFileWriter;
    ProgressDialog mProgressDialog;


    private static final String json_ads = "/sdcard/8Eggs/ads.json";
    static ImageView img_ads;
    ArrayList<HashMap<String, String>> data_ads;
    HashMap<String, String> resultp_ads = new HashMap<String, String>();
    static String ADS_IMAGE = "ads_image";
    JSONObject jsonobject_ads;
    FileWriter jsonFileWriter_ads;
    JSONObject ads;
    File file_ads;

    String ads_image;
    static ArrayList<HashMap<String, String>> arraylist_ads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        read = 0;

        eggDirectory= new File("/sdcard/8Eggs/");
        eggDirectory.mkdirs();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                condition_refresh = 1;
                if (isInternetOn() == false) {
                    Toast.makeText(getBaseContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                    swipeContainer.setRefreshing(false);
                } else {
                    new DownloadJSON().execute();
                }
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Intent intent = getIntent();
        btn_id = intent.getStringExtra("btn_id");

        if (btn_id.equals("latest_news"))        {file = new File(json_latest_news);}
        else if (btn_id.equals("news"))          {file = new File(json_news);}
        else if (btn_id.equals("world_news"))    {file = new File(json_world_news);}
        else if (btn_id.equals("election_news")) {file = new File(json_election_news);}
        else if (btn_id.equals("cartoon"))       {file = new File(json_cartoon);}
        else if (btn_id.equals("tarot"))         {file = new File(json_tarot);}
        else if (btn_id.equals("article"))       {file = new File(json_article);}
        else if (btn_id.equals("events"))        {file = new File(json_events);}

        checkfile();

    }

    private void checkfile() {

        if (!file.exists()) {
            if (isInternetOn() == false) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please switch your internet connection.")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (isInternetOn() == true) {
                                    new FirstDownloadJSON().execute();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                new FirstDownloadJSON().execute();
            }
        }
        else if (file.exists()) {
            long length = file.length();
            if (length == 0 ) {
                if (isInternetOn() == false) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Please switch your internet connection.")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (isInternetOn() == true) {
                                        new FirstDownloadJSON().execute();
                                    }
                                }
                            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    new FirstDownloadJSON().execute();
                }
            } else {
                new ReadJSON().execute();
            }
        }
    }

    private class ReadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();
            if (btn_id.equals("latest_news"))        { add_readjson_to_map(json_latest_news, "Latest News"); }
            else if (btn_id.equals("news"))          { add_readjson_to_map(json_news, "News");}
            else if (btn_id.equals("world_news"))    { add_readjson_to_map(json_world_news, "World News");}
            else if (btn_id.equals("election_news")) { add_readjson_to_map(json_election_news, "Election News"); }
            else if (btn_id.equals("cartoon"))       { add_readjson_to_map(json_cartoon, "Cartoon"); }
            else if (btn_id.equals("tarot"))         { add_readjson_to_map(json_tarot, "Tarot Magic"); }
            else if (btn_id.equals("article"))       { add_readjson_to_map(json_article, "Articles"); }
            else if (btn_id.equals("events"))        { add_readjson_to_map(json_events, "Events"); }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            listview = (ListView) findViewById(R.id.listview);
            if (btn_id.equals("cartoon")) {
                adapter_bigphoto = new ListViewAdapter_bigphoto(ListViewMain.this, arraylist);
                listview.setAdapter(adapter_bigphoto);
            } else if (btn_id.equals("tarot") || btn_id.equals("events"))  {
                adapter_tarot = new ListViewAdapter_tarot(ListViewMain.this, arraylist);
                listview.setAdapter(adapter_tarot);
            } else {
                adapter = new ListViewAdapter(ListViewMain.this, arraylist);
                listview.setAdapter(adapter);
            }
        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            condition_refresh = 1;
            if(swipeContainer.isRefreshing() == false ){
                swipeContainer.setRefreshing(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();

            arraylist_ads = new ArrayList<HashMap<String, String>>();
                if (btn_id.equals("latest_news")) {
                    add_downjson_to_map("http://www.theladiesnewsjournal.com/latest-news.txt", json_latest_news, "Latest News");
                } else if (btn_id.equals("news")) {
                    add_downjson_to_map("http://www.theladiesnewsjournal.com/news.txt", json_news, "News");
                } else if (btn_id.equals("world_news")) {
                    add_downjson_to_map("http://theladiesnewsjournal.com/world-news.txt", json_world_news, "World News");
                } else if (btn_id.equals("election_news")) {
                    add_downjson_to_map("http://theladiesnewsjournal.com/election-news.txt", json_election_news, "Election News");
                } else if (btn_id.equals("cartoon")) {
                    add_downjson_to_map("http://theladiesnewsjournal.com/cartoon.txt", json_cartoon, "Cartoon");
                } else if (btn_id.equals("tarot")) {
                    add_downjson_to_map("http://theladiesnewsjournal.com/tarot-magic.txt", json_tarot, "Tarot Magic");
                } else if (btn_id.equals("article")) {
                    add_downjson_to_map("http://theladiesnewsjournal.com/articles.txt", json_article, "Articles");
                } else if (btn_id.equals("events")) {
                    add_downjson_to_map("http://theladiesnewsjournal.com/events.txt", json_events, "Events");
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (arraylist.isEmpty()) {

            } else {
                listview = (ListView) findViewById(R.id.listview);
                if (btn_id.equals("cartoon")) {
                    adapter_bigphoto = new ListViewAdapter_bigphoto(ListViewMain.this, arraylist);
                    listview.setAdapter(adapter_bigphoto);
                } else if (btn_id.equals("tarot") || btn_id.equals("events")) {
                    adapter_tarot = new ListViewAdapter_tarot(ListViewMain.this, arraylist);
                    listview.setAdapter(adapter_tarot);
                } else {
                    adapter = new ListViewAdapter(ListViewMain.this, arraylist);
                    listview.setAdapter(adapter);
                }
            }
            if (condition_refresh == 1) {
                swipeContainer.setRefreshing(false);
            }
        }
    }

    private class FirstDownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            condition_refresh = 1;
            mProgressDialog = new ProgressDialog(ListViewMain.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Downloading contents. Please wait....");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();

            arraylist_ads = new ArrayList<HashMap<String, String>>();
            if (btn_id.equals("latest_news")) {
                add_firstdownjson_to_map("http://www.theladiesnewsjournal.com/latest-news.txt", json_latest_news, "Latest News");
            } else if (btn_id.equals("news")) {
                add_firstdownjson_to_map("http://www.theladiesnewsjournal.com/news.txt", json_news, "News");
            } else if (btn_id.equals("world_news")) {
                add_firstdownjson_to_map("http://theladiesnewsjournal.com/world-news.txt", json_world_news, "World News");
            } else if (btn_id.equals("election_news")) {
                add_firstdownjson_to_map("http://theladiesnewsjournal.com/election-news.txt", json_election_news, "Election News");
            } else if (btn_id.equals("cartoon")) {
                add_firstdownjson_to_map("http://theladiesnewsjournal.com/cartoon.txt", json_cartoon, "Cartoon");
            } else if (btn_id.equals("tarot")) {
                add_firstdownjson_to_map("http://theladiesnewsjournal.com/tarot-magic.txt", json_tarot, "Tarot Magic");
            } else if (btn_id.equals("article")) {
                add_firstdownjson_to_map("http://theladiesnewsjournal.com/articles.txt", json_article, "Articles");
            } else if (btn_id.equals("events")) {
                add_firstdownjson_to_map("http://theladiesnewsjournal.com/events.txt", json_events, "Events");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            if (arraylist.isEmpty()) {

            } else {
                listview = (ListView) findViewById(R.id.listview);
                if (btn_id.equals("cartoon")) {
                    adapter_bigphoto = new ListViewAdapter_bigphoto(ListViewMain.this, arraylist);
                    listview.setAdapter(adapter_bigphoto);
                } else if (btn_id.equals("tarot") || btn_id.equals("events"))  {
                    adapter_tarot = new ListViewAdapter_tarot(ListViewMain.this, arraylist);
                    listview.setAdapter(adapter_tarot);
                } else {
                    adapter = new ListViewAdapter(ListViewMain.this, arraylist);
                    listview.setAdapter(adapter);
                }
            }

            if (condition_refresh == 1) { mProgressDialog.dismiss(); }
        }
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


    public ArrayList add_readjson_to_map(String FileName, String jsonArrayName) {



        try {
            InputStream jsonStream = new FileInputStream(FileName);
            jsonObject = new JSONObject(InputStreamToString(jsonStream));
            jsonArray = jsonObject.getJSONArray(jsonArrayName);

            if (btn_id.equals("cartoon")) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject news = jsonArray.getJSONObject(i);

                    if (news.getString("condition").equals("2")){
                        continue;
                    } else {
                        if (read == 1) {
                            if (news.getString("condition").equals("1") || news.getString("condition").equals("3")) {
                                map.put("title", news.getString("title"));
                                map.put("createddate", news.getString("created"));
                                map.put("condition", news.getString("condition"));
                                map.put("path", news.getString("Path"));
                                map.put("field_image", news.getString("field_image"));
                            } else {
                                continue;
                            }
                        } else if (read == 2) {
                            if (news.getString("condition").equals("1") || news.getString("condition").equals("2") ||news.getString("condition").equals("3") )
                            { continue;
                            } else {
                                map.put("title", news.getString("title"));
                                map.put("createddate", news.getString("created"));
                                map.put("condition", news.getString("condition"));
                                map.put("path", news.getString("Path"));
                                map.put("field_image", news.getString("field_image"));
                            }
                        } else if (read == 0) {
                            map.put("title", news.getString("title"));
                            map.put("createddate", news.getString("created"));
                            map.put("condition", news.getString("condition"));
                            map.put("path", news.getString("Path"));
                            map.put("field_image", news.getString("field_image"));
                        } else if (read == 3) {
                            if (news.getString("condition").equals("3")) {
                                map.put("title", news.getString("title"));
                                map.put("path", news.getString("Path"));
                                map.put("condition", news.getString("condition"));
                                map.put("createddate", news.getString("created"));
                                map.put("field_image", news.getString("field_image"));
                            } else { continue; }
                        }
                        arraylist.add(map);
                    }
                }

            } else if (btn_id.equals("tarot") || btn_id.equals("events")) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject news = jsonArray.getJSONObject(i);
                    if (news.getString("condition").equals("2")){
                        continue;
                    } else {
                        if (read == 1) {
                            if (news.getString("condition").equals("1") || news.getString("condition").equals("3")) {
                                map.put("title", news.getString("title"));
                                map.put("body", news.getString("body"));
//                                if (news.getString("body").length() <= 60) {
//                                    map.put("body_small", news.getString("body") + "...");
//                                } else {
                                    map.put("body_small", news.getString("body"));
//                                }
                                map.put("path", news.getString("Path"));
                                map.put("condition", news.getString("condition"));
                                map.put("createddate", news.getString("created"));
                            } else {
                                continue;
                            }
                        } else if (read == 2) {
                            if (news.getString("condition").equals("1") || news.getString("condition").equals("2") ||news.getString("condition").equals("3") )
                            {
                                continue;
                            } else {
                                map.put("title", news.getString("title"));
                                map.put("body", news.getString("body"));
//                                if (news.getString("body").length() <= 60) {
//                                    map.put("body_small", news.getString("body") + "...");
//                                } else {
                                    map.put("body_small", news.getString("body"));
//                                }
                                map.put("path", news.getString("Path"));
                                map.put("condition", news.getString("condition"));
                                map.put("createddate", news.getString("created"));
                            }
                        } else if (read == 0) {
                            map.put("title", news.getString("title"));
                            map.put("body", news.getString("body"));
//                            if (news.getString("body").length() <= 60) {
//                                map.put("body_small", news.getString("body") + "...");
//                            } else {
                                map.put("body_small", news.getString("body"));
//                            }
                            map.put("path", news.getString("Path"));
                            map.put("condition", news.getString("condition"));
                            map.put("createddate", news.getString("created"));
                        } else if (read == 3) {
                            if (news.getString("condition").equals("3")) {
                                map.put("title", news.getString("title"));
                                map.put("body", news.getString("body"));
//                                if (news.getString("body").length() <= 60){
//                                    map.put("body_small", news.getString("body") + "...");
//                                } else {
                                    map.put("body_small", news.getString("body"));
//                                }
                                map.put("path", news.getString("Path"));
                                map.put("condition", news.getString("condition"));
                                map.put("createddate", news.getString("created"));
                            } else { continue; }
                        }
                        arraylist.add(map);
                    }
                }

            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject news = jsonArray.getJSONObject(i);

                    if (news.getString("condition").equals("2")){
                        continue;
                    } else {
                        if (read == 1) {
                            if (news.getString("condition").equals("1") || news.getString("condition").equals("3")) {
                                map.put("title", news.getString("title"));
                                map.put("body", news.getString("body"));
//                                if (news.getString("body").length() <= 60){
//                                    map.put("body_small", news.getString("body") + "...");
//                                } else {
                                    map.put("body_small", news.getString("body"));
//                                }
                                map.put("path", news.getString("Path"));
                                map.put("condition", news.getString("condition"));
                                map.put("createddate", news.getString("created"));
                                map.put("field_image", news.getString("field_image"));
                            } else { continue; }
                        } else if (read == 2) {
                            if (news.getString("condition").equals("1") || news.getString("condition").equals("2") ||news.getString("condition").equals("3") )
                            { continue; }
                            else {
                                map.put("title", news.getString("title"));
                                map.put("body", news.getString("body"));
//                                if (news.getString("body").length() <= 60){
//                                    map.put("body_small", news.getString("body") + "...");
//                                } else {
                                    map.put("body_small", news.getString("body"));
//                                }
                                map.put("path", news.getString("Path"));
                                map.put("condition", news.getString("condition"));
                                map.put("createddate", news.getString("created"));
                                map.put("field_image", news.getString("field_image"));
                            }
                        } else if (read == 0) {
                            map.put("title", news.getString("title"));
                            map.put("body", news.getString("body"));
//                            if (news.getString("body").length() <= 60){
//                                map.put("body_small", news.getString("body") + "...");
//                            } else {
                                map.put("body_small", news.getString("body"));
//                            }

                            map.put("createddate", news.getString("created"));
                            map.put("condition", news.getString("condition"));
                            map.put("path", news.getString("Path"));
                            map.put("field_image", news.getString("field_image"));
                        } else if (read == 3) {
                            if (news.getString("condition").equals("3")) {
                                map.put("title", news.getString("title"));
                                map.put("body", news.getString("body"));
//                                if (news.getString("body").length() <= 60){
//                                    map.put("body_small", news.getString("body") + "...");
//                                } else {
                                    map.put("body_small", news.getString("body"));
//                                }
                                map.put("path", news.getString("Path"));
                                map.put("condition", news.getString("condition"));
                                map.put("createddate", news.getString("created"));
                                map.put("field_image", news.getString("field_image"));
                            } else { continue; }
                        }
                    }

                    arraylist.add(map);
                }
            }

        }  catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return arraylist;
    }

    public ArrayList add_downjson_to_map(String jsonURL, String fileName, String jsonArrayName) {
        try {
            jsonobject = JSONfunctions.getJSONfromURL(jsonURL);
            jsonFileWriter = new FileWriter(fileName);
            JSONArray jsondownloadedArray = jsonobject.getJSONArray(jsonArrayName);

            if (btn_id.equals("cartoon")) {
                for (int i = 0; i < jsondownloadedArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    news = jsondownloadedArray.getJSONObject(i);


                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject readjson = jsonArray.getJSONObject(j);
                        if (news.getString("title").equals(readjson.getString("title"))) {
                            news.put("condition", readjson.getString("condition"));
                        }
                    }

                    map.put("title", news.getString("title"));
                    map.put("createddate", news.getString("created"));
                    map.put("path", news.getString("Path"));
                    map.put("condition", news.getString("condition"));
                    map.put("field_image", news.getString("field_image"));

                    arraylist.add(map);
                }

            } else if (btn_id.equals("tarot") || btn_id.equals("events")) {
                for (int i = 0; i < jsondownloadedArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    news = jsondownloadedArray.getJSONObject(i);

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject readjson = jsonArray.getJSONObject(j);
                        if (news.getString("title").equals(readjson.getString("title"))) {
                            news.put("condition", readjson.getString("condition"));
                        }
                    }

                    map.put("title", news.getString("title"));
//                    if (news.getString("body").length() <= 60){
//                        map.put("body_small", news.getString("body") + "...");
//                    } else {
                        map.put("body_small", news.getString("body"));
//                    }
                    map.put("path", news.getString("Path"));
                    map.put("condition", news.getString("condition"));
                    map.put("createddate", news.getString("created"));

                    arraylist.add(map);
                }
            } else {
                for (int i = 0; i < jsondownloadedArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    news = jsondownloadedArray.getJSONObject(i);


                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject readjson = jsonArray.getJSONObject(j);
                        if (news.getString("title").equals(readjson.getString("title"))) {
                            news.put("condition", readjson.getString("condition"));
                        }
                    }

                    map.put("title", news.getString("title"));
                    map.put("body", news.getString("body"));
//                    if (news.getString("body").length() <= 60){
//                        map.put("body_small", news.getString("body") + "...");
//                    } else {
                        map.put("body_small", news.getString("body"));
//                    }


                    map.put("createddate", news.getString("created"));

                    map.put("path", news.getString("Path"));
                    map.put("condition", news.getString("condition"));
                    map.put("field_image", news.getString("field_image"));

                    arraylist.add(map);
                }
            }
            jsonFileWriter.write(jsonobject.toString());
            jsonFileWriter.flush();
            jsonFileWriter.close();




                jsonobject_ads = JSONfunctions.getJSONfromURL("http://theladiesnewsjournal.com/mobile-ad.txt");
                jsonFileWriter_ads = new FileWriter(json_ads);

                JSONArray adsarray = jsonobject_ads.getJSONArray("Mobile Ad");

                for (int i = 0; i < adsarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    ads = adsarray.getJSONObject(i);

                    map.put("ads_image", ads.getString("field_image"));

                    arraylist_ads.add(map);
                }

                jsonFileWriter_ads.write(jsonobject_ads.toString());
                jsonFileWriter_ads.flush();
                jsonFileWriter_ads.close();





        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return  arraylist;
    }

    public ArrayList add_firstdownjson_to_map(String jsonURL, String fileName, String jsonArrayName) {
        try {
            jsonobject = JSONfunctions.getJSONfromURL(jsonURL);
            jsonFileWriter = new FileWriter(fileName);

            JSONArray jsonfirstdownloadArray = jsonobject.getJSONArray(jsonArrayName);

            if (btn_id.equals("cartoon")) {
                for (int i = 0; i < jsonfirstdownloadArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    news = jsonfirstdownloadArray.getJSONObject(i);

                    map.put("title", news.getString("title"));
                    map.put("createddate", news.getString("created"));
                    map.put("path", news.getString("Path"));
                    map.put("condition", news.getString("condition"));
                    map.put("field_image", news.getString("field_image"));

                    arraylist.add(map);
                }
            } else if (btn_id.equals("tarot") || btn_id.equals("events")) {
                for (int i = 0; i < jsonfirstdownloadArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    news = jsonfirstdownloadArray.getJSONObject(i);

                    map.put("title", news.getString("title"));
//                    if (news.getString("body").length() <= 60){
//                        map.put("body_small", news.getString("body") + "...");
//                    } else {
                        map.put("body_small", news.getString("body"));
//                    }
                    map.put("path", news.getString("Path"));
                    map.put("condition", news.getString("condition"));
                    map.put("createddate", news.getString("created"));


                    arraylist.add(map);
                }
            } else {
                for (int i = 0; i < jsonfirstdownloadArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    news = jsonfirstdownloadArray.getJSONObject(i);

                    map.put("title", news.getString("title"));
                    map.put("body", news.getString("body"));
//                    if (news.getString("body").length() <= 60){
//                        map.put("body_small", news.getString("body") + "...");
//                    } else {
                        map.put("body_small", news.getString("body"));
//                    }

                    map.put("createddate", news.getString("created"));
                    map.put("path", news.getString("Path"));
                    map.put("condition", news.getString("condition"));
                    map.put("field_image", news.getString("field_image"));

                    arraylist.add(map);
                }
            }

            jsonFileWriter.write(jsonobject.toString());
            jsonFileWriter.flush();
            jsonFileWriter.close();

                jsonobject_ads = JSONfunctions.getJSONfromURL("http://theladiesnewsjournal.com/mobile-ad.txt");
                jsonFileWriter_ads = new FileWriter(json_ads);

                JSONArray adsarray = jsonobject_ads.getJSONArray("Mobile Ad");

                for (int i = 0; i < adsarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    ads = adsarray.getJSONObject(i);

                    map.put("ads_image", ads.getString("field_image"));

                    arraylist_ads.add(map);
                }

                jsonFileWriter_ads.write(jsonobject_ads.toString());
                jsonFileWriter_ads.flush();
                jsonFileWriter_ads.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
        return  arraylist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.all)         { read = 0; new ReadJSON().execute(); }
        else if (id == R.id.read)   { read = 1;listview.setAdapter(null); new ReadJSON().execute(); }
        else if (id == R.id.unread) { read = 2; new ReadJSON().execute(); }
//        else if (id == R.id.help) {
//            LayoutInflater inflater = getLayoutInflater();
//            View view = inflater.inflate(R.layout.toast,
//                    (ViewGroup) findViewById(R.id.toast_layout_root));
//
//            Toast toast = new Toast(this);
//            toast.setView(view);
//            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();        }
        else if (id == R.id.more) { }
        else if (id == R.id.stared) { read = 3; new ReadJSON().execute(); }
        else if (id == R.id.all_star) { read = 0; new ReadJSON().execute(); }
        else {
            startActivityAfterCleanup(MainActivity.class);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }





}
