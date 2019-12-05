package com.glriverside.chyqing.memorandum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Memo extends AppCompatActivity {

    private BottomNavigationView nvMemo;
    private ImageView ivDelete;
    private ImageView ivAdd;
    private ImageView ivSet;
    private MemoOpenHelper memoOpenHelper;
    private ListView lvMemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_list);

        memoOpenHelper = new MemoOpenHelper(Memo.this);
        nvMemo = findViewById(R.id.memo_navigation);
        LayoutInflater.from(Memo.this).inflate(R.layout.memo_bottom_navigation, nvMemo, true);
        ivAdd = nvMemo.findViewById(R.id.iv_add);
        ivDelete = nvMemo.findViewById(R.id.iv_delete);
        ivSet = nvMemo.findViewById(R.id.iv_set);
        lvMemo = findViewById(R.id.lv_memo_list);

        initDb();

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.choose_input_method);
                Button virtualInput = findViewById(R.id.virtual_input);
                Button voiceInput = findViewById(R.id.voice_input);

                virtualInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Memo.this, MemoEdit.class);
                        startActivity(intent);
                    }
                });

                voiceInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent("memorandum"));
                    }
                });
            }
        });
    }

    public void initDb(){
        //创建一个MemoValues的List，保存数据库的数据
        List<MemoValues> memoValuesList = new ArrayList<>();

        //获取一个可读的数据库对象
        SQLiteDatabase db = memoOpenHelper.getReadableDatabase();

        //查询
        Cursor cursor = db.query(MemoContract.MemoEntry.TABLE_NAME, null, null,
                null, null, null, null);
        if(cursor.moveToFirst()){
            MemoValues values;
            while (!cursor.isAfterLast()){

                //实例化一个MemoValues
                values = new MemoValues();

                //将数据库中的数据赋给values
                values.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(MemoContract.MemoEntry._ID))));
                values.setTitle(cursor.getString(cursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME_TITLE)));
                values.setDate(cursor.getString(cursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME_DATE)));
                values.setContent(cursor.getString(cursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME_CONTENT_PATH)));

                //是否提醒

                //是否为待办事件

                //存入memoValuesList中
                memoValuesList.add(values);
                cursor.moveToNext();

            }
        }
        cursor.close();
        db.close();

        //设置适配器
        MemoAdapter memoAdapter = new MemoAdapter(memoValuesList, Memo.this, R.layout.memo_list_item);
        lvMemo.setAdapter(memoAdapter);
    }
}
