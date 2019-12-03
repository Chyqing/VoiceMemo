package com.glriverside.chyqing.memorandum;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Memo extends AppCompatActivity {

    private BottomNavigationView nvMemo;
    private ImageView ivDelete;
    private ImageView ivAdd;
    private ImageView ivSet;
    private MemoOpenHelper memoOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_list);

        nvMemo = findViewById(R.id.memo_navigation);
        LayoutInflater.from(Memo.this).inflate(R.layout.memo_bottom_navigation, nvMemo, true);
        ivAdd = nvMemo.findViewById(R.id.iv_add);
        ivDelete = nvMemo.findViewById(R.id.iv_delete);
        ivSet = nvMemo.findViewById(R.id.iv_set);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_memorandum);
                //memoOpenHelper = new MemoOpenHelper(Memo.this);
                startActivity(new Intent("memorandum"));
            }
        });
    }

    public void initDb(){

    }
}
