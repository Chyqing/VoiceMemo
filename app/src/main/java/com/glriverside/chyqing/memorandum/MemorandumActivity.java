package com.glriverside.chyqing.memorandum;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
//import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;

public class MemorandumActivity extends Activity {
    String mTag = "voice_activity";
    Toast mToast;
    int ret=0;
    EditText myEditText;
    private SpeechRecognizer mIat;
    private RecognizerDialog iatDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorandum);
        Log.d(mTag, "onCreate: logcat work");
        FrameLayout myFrameLayout = findViewById(R.id.frameLayout);
        myEditText = findViewById(R.id.content_res);
        Button voice_btn = findViewById(R.id.voice_button);
        Drawable drawable = getDrawable(R.drawable.voice_pic);

        mIat = SpeechRecognizer.createRecognizer(MemorandumActivity.this,mInitListener);

        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        drawable.setBounds(0,0,156,240);
        voice_btn.setCompoundDrawables(null,drawable,null,null);
        myFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    myEditText.setFocusable(true);
                    myEditText.setFocusableInTouchMode(true);
                    myEditText.requestFocus();
                //Log.d("Touch LinearLayout", "onClick: ");
            }
        });
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
            myEditText.append(text);
            myEditText.setSelection(myEditText.length());
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.e(mTag, "onError: " + speechError.getErrorCode());
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
