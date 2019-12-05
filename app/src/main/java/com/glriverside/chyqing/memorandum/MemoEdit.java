package com.glriverside.chyqing.memorandum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class MemoEdit extends AppCompatActivity {

    private MemoOpenHelper memoOpenHelper;
    private ImageView ivSave;
    private ImageView ivAlarm;
    private ImageView ivToDo;
    private EditText etTitle;
    private EditText etContent;
    private TextView tvDate;
    private BottomNavigationView nvMemoContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_content);

        etTitle = findViewById(R.id.et_memo_title);
        etContent = findViewById(R.id.et_memo_content);
        tvDate = findViewById(R.id.tv_time);

        nvMemoContent = findViewById(R.id.memo_content_navigation);
        LayoutInflater.from(MemoEdit.this).inflate(R.layout.memo_content_navigation, nvMemoContent, true);
        ivSave = nvMemoContent.findViewById(R.id.iv_save);
        ivAlarm = nvMemoContent.findViewById(R.id.iv_alarm);
        ivToDo = nvMemoContent.findViewById(R.id.iv_todo);

        //获取当前时间
        tvDate.setText(getTime());
        edit();
    }

    public void edit(){
        memoOpenHelper = new MemoOpenHelper(MemoEdit.this);
        SQLiteDatabase db = memoOpenHelper.getReadableDatabase();

        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取可写数据库对象
                SQLiteDatabase db = memoOpenHelper.getWritableDatabase();

                //实例化一个ContentValues
                ContentValues contentValues = new ContentValues();

                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();
                String time = tvDate.getText().toString();

                if ("".equals(title)){
                    Toast.makeText(MemoEdit.this, "标题不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                if ("".equals(content)){
                    Toast.makeText(MemoEdit.this, "内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                //将数据存入ContentValues
                contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_TITLE, title);
                contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_CONTENT_PATH, content);
                contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_DATE, time);

                //插入数据库
                db.insert(MemoContract.MemoEntry.TABLE_NAME, null, contentValues);

                //显示Toast消息
                Toast.makeText(MemoEdit.this, "保存成功", Toast.LENGTH_LONG).show();

                //返回显示列表
                Intent intent = new Intent(MemoEdit.this, Memo.class);
                startActivity(intent);
                db.close();
            }
        });

    }

    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = sdf.format(date);
        return str;
    }

}
