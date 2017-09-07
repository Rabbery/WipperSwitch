package com.yumpkimx.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yumpkimx.wipperswitchlib.WipperSwitchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WipperSwitchView wipperSwitchView = (WipperSwitchView) findViewById(R.id.wipperswitch);
        wipperSwitchView.addOnSwitchStateChangeListener(new WipperSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void open() {

            }

            @Override
            public void close() {

            }
        });

    }
}
