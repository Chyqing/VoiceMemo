package com.glriverside.chyqing.memorandum.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.glriverside.chyqing.memorandum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity {

    private BottomNavigationView nvRecord;
    private ImageView ivDelete;
    private ImageView ivAdd;
    private ImageView ivSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list);

        nvRecord = findViewById(R.id.record_navigation);
        LayoutInflater.from(RecordActivity.this).inflate(R.layout.memo_bottom_navigation, nvRecord, true);
        ivAdd = nvRecord.findViewById(R.id.iv_add);
        ivDelete = nvRecord.findViewById(R.id.iv_delete);
        ivSet = nvRecord.findViewById(R.id.iv_set);
    }
}
