package com.robot.asus.MusicBuddy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.robot.asus.robotactivity.RobotActivity;
import com.asus.robotframework.API.SpeakConfig;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.RobotUtil;

import org.json.JSONObject;

public class MainActivity extends RobotActivity {
    public final static String TAG = "MainActivity";
    public final static String DOMAIN = "DF0AAD95A4FB4C1480B7019B3EAE5FA6";

    private static TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // close faical
        //robotAPI.robot.setExpression(com.asus.robotframework.API.RobotFace.HIDEFACE);

        // jump dialog domain
        robotAPI.robot.jumpToPlan(DOMAIN, "ThisPlanLaunchingThisApp");

        // listen user utterance
        robotAPI.robot.speakAndListen("歡迎使用Music Buddy，請問你要在什麼狀態下聽音樂呢？", new SpeakConfig().timeout(20));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //stop listen user utterance
        robotAPI.robot.stopSpeakAndListen();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
        }

        @Override
        public void initComplete() {
            super.initComplete();

        }
    };

    public static RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {
        @Override
        public void onFinishRegister() {

        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {

        }

        @Override
        public void onSpeakComplete(String s, String s1) {

        }

        @Override
        public void onEventUserUtterance(JSONObject jsonObject) {
            String text;
            text = "onEventUserUtterance: " + jsonObject.toString();
            android.util.Log.d(TAG, text);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onResult(JSONObject jsonObject) {
            String text;
            text = "onResult: " + jsonObject.toString();
            android.util.Log.d(TAG, text);


            String sIntentionID = RobotUtil.queryListenResultJson(jsonObject, "IntentionId");
            Log.d(TAG, "Intention Id = " + sIntentionID);

            if(sIntentionID.equals("Situation")) {
                String SluResultSituation = RobotUtil.queryListenResultJson(jsonObject, "usersituation", null);
                Log.d(TAG, "Result Situation = " + SluResultSituation);

                if(SluResultSituation!= null) {
                    mTextView.setText("使用者狀態 " + SluResultSituation);
                }
            }
        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };




    public MainActivity() {
        super(robotCallback, robotListenCallback);
    }


}
