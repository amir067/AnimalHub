package com.example.animalhub.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.animalhub.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Information_Activity extends AppCompatActivity {
    private static final String TAG = "Information_Activity";

    @BindView(R.id.info)
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_);
        ButterKnife.bind(this);
        info.setText(getIntent().getStringExtra("detail"));


    }



}