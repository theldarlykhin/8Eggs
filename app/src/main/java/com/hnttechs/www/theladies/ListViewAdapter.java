package com.hnttechs.www.theladies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
    String condition_title;
    private static final String json_latest_news = "/sdcard/8Eggs/egg_latest_news.json";
    private static final String json_news = "/sdcard/8Eggs/egg_news.json";
    private static final String json_world_news = "/sdcard/8Eggs/egg_world_news.json";
    private static final String json_election_news = "/sdcard/8Eggs/egg_election_news.json";
    private static final String json_article = "/sdcard/8Eggs/egg_article.json";
    boolean switchMe = false;

	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
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
		TextView body;
		TextView createddate;
		ImageView field_image;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View itemView = inflater.inflate(R.layout.listview_item, null);

		resultp = data.get(position);
		title = (TextView) itemView.findViewById(R.id.title);
		body = (TextView) itemView.findViewById(R.id.body);
		createddate = (TextView) itemView.findViewById(R.id.createddate);
		field_image = (ImageView) itemView.findViewById(R.id.field_image);

		title.setText(resultp.get(ListViewMain.TITLE));
		body.setText(resultp.get(ListViewMain.BODY_SMALL));
		createddate.setText(resultp.get(ListViewMain.CREATEDDATE));
        if (resultp.get(ListViewMain.FIELD_IMAGE).equals(""))
        {
            field_image.setVisibility(View.GONE);
        } else {


            imageLoader.DisplayImage(resultp.get(ListViewMain.FIELD_IMAGE), field_image);
        }

		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {



                condition_title = title.getText().toString();

                if(ListViewMain.btn_id.equals("latest_news"))         { CheckJSON_method(position,json_latest_news, "Latest News");  }
                else if (ListViewMain.btn_id.equals("news"))          { CheckJSON_method(position,json_news, "News");                }
                else if (ListViewMain.btn_id.equals("world_news"))    { CheckJSON_method(position,json_world_news, "World News"); }
                else if (ListViewMain.btn_id.equals("election_news")) { CheckJSON_method(position,json_election_news, "Election News"); }
                else if (ListViewMain.btn_id.equals("article"))       { CheckJSON_method(position,json_article, "Articles"); }
			}
		});

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(50);

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                condition_title = title.getText().toString();

                                if(ListViewMain.btn_id.equals("latest_news"))         { delete_item(json_latest_news, "Latest News");  }
                                else if (ListViewMain.btn_id.equals("news"))          { delete_item(json_news, "News");                }
                                else if (ListViewMain.btn_id.equals("world_news"))    { delete_item(json_world_news, "World News"); }
                                else if (ListViewMain.btn_id.equals("election_news")) { delete_item(json_election_news, "Election News"); }
                                else if (ListViewMain.btn_id.equals("article"))       { delete_item(json_article, "Articles"); }


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
		return itemView;
	}


    public void CheckJSON_method(int position, String fileName, String jsonArrayName) {

        try {
            InputStream jsonStream = new FileInputStream(fileName);
            JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
            JSONArray jsonreadArray = jsonObject.getJSONArray(jsonArrayName);

            for (int i = 0; i < jsonreadArray.length(); i++) {
                JSONObject latestnews = jsonreadArray.getJSONObject(i);
                if (latestnews.getString("title").equals(condition_title)) {

                    JSONArray jsonwriteArray = jsonObject.getJSONArray(jsonArrayName);
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


        resultp = data.get(position);
        Intent intent = new Intent(context, SingleItemView.class);

        intent.putExtra("title", resultp.get(ListViewMain.TITLE));
        intent.putExtra("body", resultp.get(ListViewMain.BODY));
        intent.putExtra("createddate",resultp.get(ListViewMain.CREATEDDATE));
        intent.putExtra("path",resultp.get(ListViewMain.PATH));
        intent.putExtra("condition",resultp.get(ListViewMain.CONDITION));
        intent.putExtra("field_image", resultp.get(ListViewMain.FIELD_IMAGE));
        intent.putExtra("position", position);


        context.startActivity(intent);

    }


    public void delete_item(String fileName, String jsonArrayName) {

        try {
            InputStream jsonStream = new FileInputStream(fileName);
            JSONObject jsonObject = new JSONObject(ListViewMain.InputStreamToString(jsonStream));
            JSONArray jsonreadArray = jsonObject.getJSONArray(jsonArrayName);

            for (int i = 0; i < jsonreadArray.length(); i++) {
                JSONObject latestnews = jsonreadArray.getJSONObject(i);
                if (latestnews.getString("title").equals(condition_title)) {
                    JSONArray jsonwriteArray = jsonObject.getJSONArray(jsonArrayName);
                    for (int j = 0; j < jsonwriteArray.length(); j++) {
                        JSONObject writenews = jsonreadArray.getJSONObject(j);
                        if (writenews.getString("title").equals(condition_title)) {
                            writenews.put("condition", "2");
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

}
