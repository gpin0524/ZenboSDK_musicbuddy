package com.robot.asus.MusicBuddy;

import android.os.Bundle;
import android.util.Log;


import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.util.List;


public class YoutubeActivity extends RobotActivity {
    private static final String TAG = "YoutubePlayerActivity";

    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    private static String listId; //等待傳值

    public YoutubeActivity() {
        super(robotCallback, robotListenCallback);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);


        // 初始化YoutubePlayer開始
        Log.d(TAG, "onCreate: Starting. ListId = " + listId);
        mYoutubePlayerView = findViewById(R.id.youTubePlayerView);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onCreate: Done Initializing .");

                youTubePlayer.loadPlaylist(listId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onCreate: Failed to Initializing .");
            }
        };

        Log.d(TAG, "onCreate: Initializing youtube player...");
        mYoutubePlayerView.initialize(YoutubeConfig.getApiKey(), mOnInitializedListener);
        // 初始化YoutubePlayer結束
    }

    // Robot : start
    @Override
    protected void onResume() {
        super.onResume();
        robotAPI.robot.speak("好喔，馬上為你破放清單");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {

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

        }

        @Override
        public void onResult(JSONObject jsonObject) {

        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };
    // Robot : end


}
