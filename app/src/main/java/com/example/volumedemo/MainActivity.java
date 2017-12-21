package com.example.volumedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private VolumeView view;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        view = (VolumeView) findViewById(R.id.volumeView);
        view.setOnChangeListener(new VolumeView.OnChangeListener() {

            @Override
            public void onChange(int count) {
                tv.setText("当前音量："+count);
            }
        });
    }
}
