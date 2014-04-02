package com.yujinrobot.gimbalapp.test_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qualcommlabs.usercontext.Callback;
import com.qualcommlabs.usercontext.ContextCoreConnector;
import com.qualcommlabs.usercontext.ContextCoreConnectorFactory;
import com.qualcommlabs.usercontext.ContextPlaceConnector;
import com.qualcommlabs.usercontext.ContextPlaceConnectorFactory;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.PlacePerformanceLevel;
import com.qualcommlabs.usercontext.protocol.Place;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ContextCoreConnector contextCoreConnector;
    private ContextPlaceConnector contextPlaceConnector;
    private Activity m_this;
    private int m_log_count = 0;
    private int m_maximum_log_count = 100;

    private ArrayList<String> m_debug_log_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_this = this;
        setContentView(R.layout.activity_main);

        //core connect test
        contextCoreConnector = ContextCoreConnectorFactory.get(this);
        //Geofense test
        contextPlaceConnector = ContextPlaceConnectorFactory.get(this);
        checkContextConnectorStatus();
        m_debug_log_list = new ArrayList<String>();
        sendLog("Where am I?");
    }

    private void checkContextConnectorStatus() {
        if (contextCoreConnector.isPermissionEnabled()) {
            // Gimbal is already enabled
            Log.d("[GB_APP]","Gimbal is already enabled");
            startListeningForGeofences();
        }
        else {
            contextCoreConnector.enable(this, new Callback<Void>() {

                @Override
                public void success(Void arg0) {
                    // Gimbal is ready
                    Log.d("[GB_APP]","Gimbal is ready: "+ arg0);
                    startListeningForGeofences();
                }

                @Override
                public void failure(int arg0, String arg1) {
                    Log.d("[GB_APP]","failed to enable: "+ arg1);
                }
            });
        }
    }

    private void startListeningForGeofences() {
        contextPlaceConnector.addPlaceEventListener(placeEventListener);
    }

    private PlaceEventListener placeEventListener = new PlaceEventListener() {
        @Override
        public void placeEvent(PlaceEvent event) {
            String placeNameAndId = "id: " + event.getPlace().getId() + " name: " + event.getPlace().getPlaceName();
            Toast toast = Toast.makeText(getApplicationContext(), placeNameAndId, Toast.LENGTH_LONG);
            toast.show();
            Log.d("[GB_APP]", "Place Event!!!: " + placeNameAndId);
            sendLog("I'm in "+placeNameAndId);

        }
    };

    private Button.OnClickListener btnOnClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View arg) {
                switch (arg.getId()) {
                    default:
                        break;
                }
            }
    };

    public void sendLog(String log){
        m_log_count++;
        String strLog = m_log_count+ ": "+log+ "\n";
        m_debug_log_list.add(0,strLog);
        if(m_log_count >= m_maximum_log_count){
            m_debug_log_list.remove(m_debug_log_list.size()-1);
        }
        TextView tv = (TextView)findViewById(R.id.place);
        tv.setText(m_debug_log_list.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}