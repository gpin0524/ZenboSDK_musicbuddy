package com.robot.asus.MusicBuddy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotCommand;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.RobotUtil;
import com.asus.robotframework.API.WheelLights;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class YoutubeActivity extends RobotActivity {
    private static final String TAG = "GpinYoutubePlayerAc";

    YouTubePlayerFragment mYouTubePlayerFragment;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    // firebase
    FirebaseFirestore db;

    // 變數
    private static String userId;
    String situationListId;
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
        Log.d(TAG, "onCreate: 接值 userId = " + userId + "situation = " + situation);

        // callback
        getSituationListId(userId, situation, new situationListIdCallback() {
            @Override
            public void onCallback(final String situationListId) {
                Log.d(TAG, "onCallback: situationListId  = " + situationListId);
                // 初始化YoutubePlayer開始
                Log.d(TAG, "onCreate: Starting. ListId = " + situationListId);


                mYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
                mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        Log.d(TAG, "onCreate: Done Initializing .");

                        youTubePlayer.loadPlaylist(situationListId);
                        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {
                                robotAPI.robot.setExpression(RobotFace.EXPECTING);
                            }

                            @Override
                            public void onLoaded(String s) {

                            }

                            @Override
                            public void onAdStarted() {

                            }

                            @Override
                            public void onVideoStarted() {
                                robotAPI.robot.setExpression(RobotFace.SINGING);
                                robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0xffff00);
                                robotAPI.wheelLights.startBlinking(WheelLights.Lights.SYNC_BOTH,10,5,5,7);
                                robotAPI.wheelLights.setBrightness(WheelLights.Lights.ASYNC_RIGHT,0xff, 25);
                                List<Integer> playActionItemList = new ArrayList<>();
                                playActionItemList.add(21);
                                playActionItemList.add(24);
                                playActionItemList.add(27);
                                int action = playActionItemList.get((int)(Math.random()*(playActionItemList.size() - 1)));
                                robotAPI.utility.playAction(action);
                                Log.d(TAG, "onVideoStarted: action = " + action);

                            }

                            @Override
                            public void onVideoEnded() {
                                robotAPI.robot.setExpression(RobotFace.DEFAULT);
                                robotAPI.cancelCommand(RobotCommand.MOTION_PLAY_ACTION.getValue());
                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {

                            }
                        });

                        youTubePlayer.setPlaylistEventListener(new YouTubePlayer.PlaylistEventListener() {
                            @Override
                            public void onPrevious() {

                            }

                            @Override
                            public void onNext() {

                            }

                            @Override
                            public void onPlaylistEnded() {
                                robotAPI.robot.setExpression(RobotFace.DEFAULT);
                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d(TAG, "onCreate: Failed to Initializing .");
                    }
                };

                Log.d(TAG, "onCreate: Initializing youtube player...");

                mYouTubePlayerFragment.initialize(YoutubeConfig.getApiKey(), mOnInitializedListener);
                //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.add(R.id.youtube_fragment, mYouTubePlayerFragment).commit();

                // 初始化YoutubePlayer結束

            }
        });


    }

    // Robot : start
    @Override
    protected void onResume() {
        super.onResume();
        robotAPI.robot.speak("好喔，馬上為你播放清單");
        robotAPI.robot.setExpression(RobotFace.DEFAULT);
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
