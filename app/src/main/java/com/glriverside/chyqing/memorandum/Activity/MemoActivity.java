package com.glriverside.chyqing.memorandum.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.glriverside.chyqing.memorandum.Adapter.MemoAdapter;
import com.glriverside.chyqing.memorandum.Contract.MemoContract;
import com.glriverside.chyqing.memorandum.Manager.MemoOpenHelper;
import com.glriverside.chyqing.memorandum.Values.MemoValues;
import com.glriverside.chyqing.memorandum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {

    private BottomNavigationView nvMemo;
    private ImageView ivDelete;
    private ImageView ivAdd;
    private ImageView ivSet;
    private MemoOpenHelper memoOpenHelper;
    private ListView lvMemo;
    public static final String MODEL = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_list);

        memoOpenHelper = new MemoOpenHelper(MemoActivity.this);
        nvMemo = findViewById(R.id.memo_navigation);
        LayoutInflater.from(MemoActivity.this).inflate(R.layout.memo_bottom_navigation, nvMemo, true);
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
                        Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
                        intent.putExtra(MemoActivity.MODEL, "false");
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

        //点击条例查询
        lvMemo.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
                MemoValues memoValues = (MemoValues)lvMemo.getItemAtPosition(i);
                intent.putExtra(MemoContract.MemoEntry._ID, memoValues.getId().toString());
                intent.putExtra(MemoContract.MemoEntry.COLUMN_NAME_TITLE, memoValues.getTitle());
               // intent.putExtra(MemoContract.MemoEntry.COLUMN_NAME_DATE, memoValues.getDate());
                intent.putExtra(MemoContract.MemoEntry.COLUMN_NAME_CONTENT_PATH, memoValues.getContent());
               // intent.putExtra(MemoContract.MemoEntry.COLUMN_NAME_ALARM, memoValues.getAlarm().toString());
                intent.putExtra(MemoContract.MemoEntry.COLUMN_NAME_ALARM_TIME, memoValues.getAlarmTime());
               // intent.putExtra(MemoContract.MemoEntry.COLUMN_NAME_TODO, memoValues.getToDo().toString());
                intent.putExtra(MemoActivity.MODEL, "true");
                startActivity(intent);
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
        MemoAdapter memoAdapter = new MemoAdapter(memoValuesList, MemoActivity.this, R.layout.memo_list_item);
        lvMemo.setAdapter(memoAdapter);
    }
}
