package com.glriverside.chyqing.memorandum;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;

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
    private Boolean isAlarm = false;
    private Boolean isToDo = false;

    String mTag = "voice_activity";
    Toast mToast;
    int ret=0;
    //EditText myEditText;
    private SpeechRecognizer mIat;
    private RecognizerDialog iatDialog;

    private AlarmManager amMemo;

    private View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //获取可写数据库对象
            SQLiteDatabase db = memoOpenHelper.getWritableDatabase();

            //实例化一个ContentValues
            ContentValues contentValues = new ContentValues();

            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();
            String time = tvDate.getText().toString();
            String alarmTime = "";

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
            contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_ALARM,isAlarm);
            contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_TODO, isToDo);

            if(isAlarm == true){
                contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_ALARM_TIME, alarmTime);
            }

            //插入数据库
            db.insert(MemoContract.MemoEntry.TABLE_NAME, null, contentValues);

            //显示Toast消息
            Toast.makeText(MemoEdit.this, "保存成功", Toast.LENGTH_LONG).show();

            //返回显示列表
            Intent intent = new Intent(MemoEdit.this, Memo.class);
            startActivity(intent);
            db.close();
        }
    };

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

        Log.d(mTag, "onCreate: logcat work");
       // FrameLayout myFrameLayout = findViewById(R.id.frameLayout);
       // myEditText = nvMemoContent.findViewById(R.id.et_memo_content);
        //Button voice_btn = findViewById(R.id.voice_button);
        ImageView ivVoice = nvMemoContent.findViewById(R.id.iv_voice);
        /*Drawable drawable = getDrawable(R.drawable.voice_pic);*/

        mIat = SpeechRecognizer.createRecognizer(MemoEdit.this,mInitListener);

        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        /*drawable.setBounds(0,0,156,240);
        ivVoice.setCompoundDrawables(null,drawable,null,null);*/
        /*myFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditText.setFocusable(true);
                myEditText.setFocusableInTouchMode(true);
                myEditText.requestFocus();
                //Log.d("Touch LinearLayout", "onClick: ");
            }
        });*/

    }

    public void edit(){
        memoOpenHelper = new MemoOpenHelper(MemoEdit.this);
        SQLiteDatabase db = memoOpenHelper.getReadableDatabase();

        //保存
        ivSave.setOnClickListener(save);

        ivAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAlarm = !isAlarm;
                if (isAlarm == true){
                    //设置时间
                }else{
                    //取消
                }

            }
        });

        ivToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isToDo = !isToDo;
                if (isToDo ==true){
                    //后台显示
                }else{
                    //不显示
                }
            }
        });

    }

    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = sdf.format(date);
        return str;
    }

    public void voice_btn_click(View view) {
        if(mIat == null){
            this.showTip("创建对象失败");
            Log.d(mTag, "voice_btn_click: ");
            return;
        }
        setParam();
        ret = mIat.startListening(recognizerListener);
        if(ret!= ErrorCode.SUCCESS){
            Log.d("voice_btn", "failure");
        }
        else{
            Log.d("voice_btn", "begin");
        }
        //Log.d("voice_btn", "voice_btn_click: ");
    }

    public void showTip(final String str){
        mToast.setText(str);
        mToast.show();
    }

    public void setParam(){
        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR,null);
        mIat.setParameter(SpeechConstant.SUBJECT,null);
        mIat.setParameter(SpeechConstant.RESULT_TYPE,"json");
        mIat.setParameter(SpeechConstant.ENGINE_TYPE,"cloud");
        mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT,"mandarin");
        mIat.setParameter(SpeechConstant.VAD_BOS,"4000");
        mIat.setParameter(SpeechConstant.VAD_EOS,"1000");
        mIat.setParameter(SpeechConstant.ASR_PTT,"1");
    }

    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            showTip("当前正在说话，音量：" + i);
        }

        @Override
        public void onBeginOfSpeech() {
            showTip("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String text = JsonParser.parseIatResult(recognizerResult.getResultString());
            etContent.append(text);
            etContent.setSelection(etContent.length());
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.e(mTag, "onError: " );
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            Log.e(mTag, "onEvent: " );
        }
    };

    //初始化语音听写对象
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            Log.d(mTag, "onInit: "+i);
            if(i!=ErrorCode.SUCCESS){
                showTip("Failure");
            }
        }
    };

}
