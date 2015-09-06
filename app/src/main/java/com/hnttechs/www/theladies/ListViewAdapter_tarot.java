package com.hnttechs.www.theladies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 7/4/15.
 */
public class ListViewAdapter_tarot extends BaseAdapter {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    String condition_title;
    String condition_for_putextra;
    private static final String json_tarot = "/sdcard/8Eggs/egg_tarot.json";
    private static final String json_events = "/sdcard/8Eggs/egg_events.json";

    public ListViewAdapter_tarot(Context context,
                                 ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView title;
        TextView createddate;
        TextView body;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item_tarot, parent, false);
        }


        resultp = data.get(position);
        title = (TextView) convertView.findViewById(R.id.title);
        body = (TextView) convertView.findViewById(R.id.body);
        createddate = (TextView) convertView.findViewById(R.id.createddate);

        title.setText(resultp.get(ListViewMain.TITLE));
        body.setText(resultp.get(ListViewMain.BODY_SMALL));
        createddate.setText(resultp.get(ListViewMain.CREATEDDATE));
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                condition_title = title.getText().toString();

                if(ListViewMain.btn_id.equals("tarot")) {

                    try {
                        InputStream jsonStream = new FileInputStream(json_tarot);
                        JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
                        JSONArray jsonreadArray = jsonObject.getJSONArray("Tarot Magic");

                        for (int i = 0; i < jsonreadArray.length(); i++) {
                            JSONObject latestnews = jsonreadArray.getJSONObject(i);
                            if (latestnews.getString("title").equals(condition_title)) {

                                FileWriter jsonFileWriter = new FileWriter(json_tarot);

                                JSONArray jsonwriteArray = jsonObject.getJSONArray("Tarot Magic");
                                for (int j = 0; j < jsonwriteArray.length(); j++) {
                                    JSONObject writenews = jsonreadArray.getJSONObject(j);
                                    if (writenews.getString("title").equals(condition_title)) {
                                        if (writenews.getString("condition").equals("0") || writenews.getString("condition").equals("0\n")){
                                            writenews.put("condition", "1");
                                        } else {
                                            writenews.put("condition", writenews.getString("condition"));
                                        }
                                    }
                                }
                                jsonFileWriter.write(jsonObject.toString());
                                jsonFileWriter.flush();
                                jsonFileWriter.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                    resultp = data.get(position);
                    Intent intent = new Intent(context, SingleItemView_tarot.class);
                    intent.putExtra("title", resultp.get(ListViewMain.TITLE));
                    intent.putExtra("body", resultp.get(ListViewMain.BODY));
                    intent.putExtra("path",resultp.get(ListViewMain.PATH));
                    intent.putExtra("condition",resultp.get(ListViewMain.CONDITION));
                    intent.putExtra("createddate",resultp.get(ListViewMain.CREATEDDATE));
                    intent.putExtra("position", position);

                    context.startActivity(intent);

                }
                else if (ListViewMain.btn_id.equals("events")) {
                    try {
                        InputStream jsonStream = new FileInputStream(json_tarot);
                        JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
                        JSONArray jsonreadArray = jsonObject.getJSONArray("Tarot Magic");

                        for (int i = 0; i < jsonreadArray.length(); i++) {
                            JSONObject latestnews = jsonreadArray.getJSONObject(i);
                            if (latestnews.getString("title").equals(condition_title)) {

                                FileWriter jsonFileWriter = new FileWriter(json_tarot);

                                JSONArray jsonwriteArray = jsonObject.getJSONArray("Tarot Magic");
                                for (int j = 0; j < jsonwriteArray.length(); j++) {
                                    JSONObject writenews = jsonreadArray.getJSONObject(j);
                                    if (writenews.getString("title").equals(condition_title)) {
                                        if (writenews.getString("condition").equals("0") || writenews.getString("condition").equals("0\n")){
                                            writenews.put("condition", "1");
                                        } else {
                                            writenews.put("condition", writenews.getString("condition"));
                                        }
                                    }
                                }
                                jsonFileWriter.write(jsonObject.toString());
                                jsonFileWriter.flush();
                                jsonFileWriter.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                    resultp = data.get(position);
                    Intent intent = new Intent(context, SingleItemView_tarot.class);
                    intent.putExtra("title", resultp.get(ListViewMain.TITLE));
                    intent.putExtra("body", resultp.get(ListViewMain.BODY));
                    intent.putExtra("path",resultp.get(ListViewMain.PATH));
                    intent.putExtra("condition",resultp.get(ListViewMain.CONDITION));
                    intent.putExtra("createddate",resultp.get(ListViewMain.CREATEDDATE));
                    intent.putExtra("position", position);

                    context.startActivity(intent);
                }

            }
        });



        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                condition_title = title.getText().toString();
                                if (ListViewMain.btn_id.equals("tarot")) {
                                    try {
                                        InputStream jsonStream = new FileInputStream(json_tarot);
                                        JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
                                        JSONArray jsonreadArray = jsonObject.getJSONArray("Tarot Magic");

                                        for (int i = 0; i < jsonreadArray.length(); i++) {
                                            JSONObject latestnews = jsonreadArray.getJSONObject(i);
                                            if (latestnews.getString("title").equals(condition_title)) {
                                                JSONArray jsonwriteArray = jsonObject.getJSONArray("Tarot Magic");
                                                for (int j = 0; j < jsonwriteArray.length(); j++) {
                                                    JSONObject writenews = jsonreadArray.getJSONObject(j);
                                                    if (writenews.getString("title").equals(condition_title)) {
                                                        writenews.put("condition", "2");
                                                    }
                                                }
                                            }
                                        }

                                        FileWriter jsonFileWriter = new FileWriter(json_tarot);
                                        jsonFileWriter.write(jsonObject.toString());
                                        jsonFileWriter.flush();
                                        jsonFileWriter.close();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        Log.e("Error", e.getMessage());
                                        e.printStackTrace();
                                    }

                                } else if (ListViewMain.btn_id.equals("events")) {
                                    try {
                                        InputStream jsonStream = new FileInputStream(json_tarot);
                                        JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
                                        JSONArray jsonreadArray = jsonObject.getJSONArray("Tarot Magic");

                                        for (int i = 0; i < jsonreadArray.length(); i++) {
                                            JSONObject latestnews = jsonreadArray.getJSONObject(i);
                                            if (latestnews.getString("title").equals(condition_title)) {
                                                JSONArray jsonwriteArray = jsonObject.getJSONArray("Tarot Magic");
                                                for (int j = 0; j < jsonwriteArray.length(); j++) {
                                                    JSONObject writenews = jsonreadArray.getJSONObject(j);
                                                    if (writenews.getString("title").equals(condition_title)) {
                                                        writenews.put("condition", "2");
                                                    }
                                                }
                                            }
                                        }

                                        FileWriter jsonFileWriter = new FileWriter(json_tarot);
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

                                ListViewMain.arraylist.remove(position);  //remove item from listview

                                notifyDataSetChanged();                   //remove item from listview
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });


        return convertView;
    }
}