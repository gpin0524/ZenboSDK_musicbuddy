package com.robot.asus.MusicBuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.util.List;


public class YoutubeActivity extends RobotActivity {
    private static final String TAG = "YoutubePlayerActivity";

    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    // firebase
    FirebaseFirestore db;

    // 變數
    private static String userId;
    private static String situationListId;
    private static String situation;

    public YoutubeActivity() {
        super(robotCallback, robotListenCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        db = FirebaseFirestore.getInstance();
        // 接值
        Intent i = this.getIntent();
        userId = i.getStringExtra("userId");
        situation= i.getStringExtra("situation");
        Log.d(TAG, "onCreate: userId = " + userId + "situation = " + situation);
        getSituationListId(userId, situation, new situationListIdCallback() {
            @Override
            public void onCallback(String situationListId) {
                Log.d(TAG, "onCallback: situationListId  = " + situationListId);
            }
        });

        // 初始化YoutubePlayer開始
        Log.d(TAG, "onCreate: Starting. ListId = " + situationListId);
        mYoutubePlayerView = findViewById(R.id.youTubePlayerView);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onCreate: Done Initializing .");

                youTubePlayer.loadPlaylist(situationListId);
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
        robotAPI.robot.speak("好喔，馬上為你播放清單");
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

    //DB
    public interface situationListIdCallback {
        void onCallback(String situationListId);
    }

    private void getSituationListId(final String userId, final String situation, final YoutubeActivity.situationListIdCallback callback) {

        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        situationListId = document.getData().get(situation).toString();
                        Log.d(TAG, "DocumentSnapshot data: situationListId = " + situationListId);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                    callback.onCallback(situationListId);
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
