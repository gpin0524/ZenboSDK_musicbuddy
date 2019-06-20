package com.robot.asus.MusicBuddy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import com.asus.robotframework.API.SpeakConfig;
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
import com.google.firebase.firestore.auth.User;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class YoutubeActivity extends RobotActivity {
    public final static String DOMAIN = "DF0AAD95A4FB4C1480B7019B3EAE5FA6";
    private static final String TAG = "GpinYoutubePlayerAc";

    private static YouTubePlayerFragment mYouTubePlayerFragment;
    private static YouTubePlayer.OnInitializedListener mOnInitializedListener;

    // firebase
    private FirebaseFirestore db;

    // 變數
    private static Context context;
    private static String userId;
    private String situationListId;
    private static String situation;
    private static Users userData = new Users();

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
            public void onCallback(final Users userData) {
                Log.d(TAG, "onCallback: situationListId  = " + userData.getListId(situation));
                // 初始化YoutubePlayer開始
                Log.d(TAG, "onCreate: Starting. ListId = " + userData.getListId(situation));


                mYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
                mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        Log.d(TAG, "onCreate: Done Initializing .");

                        youTubePlayer.loadPlaylist(userData.getListId(situation));
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
                                switch (situation){
                                    case "Work":
                                        robotAPI.robot.setExpression(RobotFace.INTERESTED);
                                        robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0x99ffff);
                                        robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0x0000cc);
                                        robotAPI.wheelLights.startBlinking(WheelLights.Lights.SYNC_BOTH,10,10,10,5);
                                        robotAPI.wheelLights.setBrightness(WheelLights.Lights.SYNC_BOTH,0xff, 45);
                                        List<Integer> playActionItemListW = new ArrayList<>();
                                        playActionItemListW.add(25); // Shake_head_4_loop
                                        playActionItemListW.add(26); // Head_twist_1_loop
                                        int actionW = playActionItemListW.get((int)(Math.random()*(playActionItemListW.size() - 1)));
                                        robotAPI.utility.playAction(actionW);
                                        break;

                                    case "Relax":
                                        //
                                        robotAPI.robot.setExpression(RobotFace.SINGING);
                                        robotAPI.wheelLights.setBrightness(WheelLights.Lights.SYNC_BOTH,0xff, 50);
                                        robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0xf0ff00);
                                        robotAPI.wheelLights.startMarquee(WheelLights.Lights.SYNC_BOTH,WheelLights.Direction.DIRECTION_BACKWARD,5,5,0);
                                        //robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0xffff00);
                                        //robotAPI.wheelLights.startBlinking(WheelLights.Lights.SYNC_BOTH,10,5,5,7);
                                        List<Integer> playActionItemListR = new ArrayList<>();
                                        playActionItemListR.add(15); // Dance_b_1_loop 點頭轉圈
                                        playActionItemListR.add(24); //  Dance_2_loop  搖頭轉圈

                                        int actionR = playActionItemListR.get((int)(Math.random()*(playActionItemListR.size() - 1)));
                                        robotAPI.utility.playAction(actionR);
                                        Log.d(TAG, "onVideoStarted: action = " + actionR);
                                        break;
                                    case "Sleep":
                                        robotAPI.robot.setExpression(RobotFace.LAZY);
                                        robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0x00d031);
                                        robotAPI.wheelLights.startBlinking(WheelLights.Lights.SYNC_BOTH,10,5,5,7);
                                        robotAPI.wheelLights.setBrightness(WheelLights.Lights.SYNC_BOTH,0xff, 35);
                                        robotAPI.wheelLights.startCharging(WheelLights.Lights.SYNC_BOTH,0,1,WheelLights.Direction.DIRECTION_FORWARD,20);
                                        robotAPI.wheelLights.startBreathing(WheelLights.Lights.SYNC_BOTH,0xFF9000,20,10,0);
                                        List<Integer> playActionItemList = new ArrayList<>();
                                        playActionItemList.add(21); // Dance_s_1_loop  點頭

                                        int actionS = playActionItemList.get((int)(Math.random()*(playActionItemList.size() - 1)));
                                        robotAPI.utility.playAction(actionS);
                                        break;
                                    case "Depress":
                                        robotAPI.wheelLights.turnOff(WheelLights.Lights.SYNC_BOTH,0xff);
                                        robotAPI.robot.setExpression(RobotFace.INNOCENT);
                                        robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0xff66b2);
                                        robotAPI.wheelLights.setColor(WheelLights.Lights.SYNC_BOTH,0xff,0xff00ff);
                                        robotAPI.wheelLights.startBlinking(WheelLights.Lights.SYNC_BOTH,10,5,5,7);
                                        robotAPI.wheelLights.setBrightness(WheelLights.Lights.SYNC_BOTH,0xff, 25);
                                        robotAPI.wheelLights.startCharging(WheelLights.Lights.SYNC_BOTH,3,2,WheelLights.Direction.DIRECTION_FORWARD,20);

                                        List<Integer> playActionItemListD = new ArrayList<>();
                                        playActionItemListD.add(17); // Music_1_loop 點頭擺動
                                        int action = playActionItemListD.get((int)(Math.random()*(playActionItemListD.size() - 1)));
                                        robotAPI.utility.playAction(action);
                                        Log.d(TAG, "onVideoStarted: action = " + action);
                                        break;
                                }
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
        robotAPI.robot.jumpToPlan(DOMAIN, "ControlMusic");
        robotAPI.robot.speak("好喔，馬上為你播放歌曲");
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
            String text;
            text = "onEventUserUtterance: " + jsonObject.toString();
            android.util.Log.d(TAG, text);
        }

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
                    situation = SluResultSituation;

                    mYouTubePlayerFragment.initialize(YoutubeConfig.getApiKey(), mOnInitializedListener);
                    Log.d(TAG, "onResult: situation = " + situation);
                }
            }
        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };
    // Robot : end

    //DB
    public interface situationListIdCallback {
        void onCallback(Users userData);
    }

    private void getSituationListId(final String userId, final String situation, final YoutubeActivity.situationListIdCallback callback) {

        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        userData = document.toObject(Users.class);
                        Log.d(TAG, "DocumentSnapshot data: situationListId = " + userData.getListId(situation));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                    callback.onCallback(userData);
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
