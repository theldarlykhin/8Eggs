package com.hnttechs.www.theladies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {


//    private AnimatedGifImageView animatedGifImageView;
//    boolean switchMe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
//        animatedGifImageView = ((AnimatedGifImageView)findViewById(R.id.animatedGifImageView));
//        animatedGifImageView.setAnimatedGif(R.drawable.loading,
//                AnimatedGifImageView.TYPE.FIT_CENTER);
////        ((Button) findViewById(R.id.button1)).setOnClickListener(this);
//        switchMe = true;
//
//        TextView lblhnttechs = (TextView)findViewById(R.id.hnttechs);
//        lblhnttechs.setPaintFlags(lblhnttechs.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        lblhnttechs.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Intent intent = new Intent(getBaseContext(),AboutUs_Activity.class);
//                startActivity(intent);
//
//                return true;
//            }
//        });

        ImageView btn_latest_news = (ImageView)findViewById(R.id.latestnews);
        ImageView btn_news = (ImageView)findViewById(R.id.news);
        ImageView btn_world_news = (ImageView)findViewById(R.id.worldnews);
        ImageView btn_election_news = (ImageView)findViewById(R.id.electionnews);
        ImageView btn_cartoon = (ImageView)findViewById(R.id.cartoon);
        ImageView btn_tarot = (ImageView)findViewById(R.id.tarot);
        ImageView btn_article = (ImageView)findViewById(R.id.article);
        ImageView btn_events = (ImageView)findViewById(R.id.events);

        btn_latest_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","latest_news");
                startActivity(intent);
            }
        });

        btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","news");
                startActivity(intent);
            }
        });
        btn_world_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","world_news");
                startActivity(intent);
            }
        });
        btn_election_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","election_news");
                startActivity(intent);
            }
        });
        btn_cartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","cartoon");
                startActivity(intent);
            }
        });
        btn_tarot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","tarot");
                startActivity(intent);
            }
        });
        btn_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","article");
                startActivity(intent);
            }
        });
        btn_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id","events");
                startActivity(intent);
            }
        });

    }

}
