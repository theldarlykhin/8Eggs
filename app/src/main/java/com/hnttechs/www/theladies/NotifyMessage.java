package com.hnttechs.www.theladies;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

/**
 * Created by dell on 6/10/15.
 */
public class NotifyMessage extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);

        TextView txt=new TextView(this);

        txt.setText("Activity after click on notification");
        setContentView(txt);
    }
}
