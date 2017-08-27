package com.almagems.cubetraz;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import static com.google.android.gms.ads.AdRequest.*;


public final class MainActivity extends Activity implements RewardedVideoAdListener {
    private RewardedVideoAd mAd;
    private String adError = "Ad is not loaded";

    private GLSurfaceView glSurfaceView;
    private MainRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity.onCreate");
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout);

        glSurfaceView = (GLSurfaceView)findViewById(R.id.surfaceView);

        //glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(1);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 24, 8);
        //setContentView(glSurfaceView);

        // Use an activity context to get the rewarded video instance.
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        renderer = new MainRenderer(this);
        glSurfaceView.setRenderer(renderer);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if (event != null) {
                    final int action = event.getAction();
                    final float x = event.getX();
                    final float y = event.getY();
                    final int fingerCount = event.getPointerCount();

                    if (action == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                Game.handleTouchPress(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                Game.handleTouchDrag(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_UP) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                Game.handleTouchRelease(x, y);
                            }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
        loadRewardedVideoAd();
    }

    @Override
    protected void onPause() {
        System.out.println("MainActivity.onPause");
        super.onPause();

        if (renderer != null) {
            glSurfaceView.onPause();
            Audio.release();
            Game.fboLost = true;
        }
    }

    @Override
    protected void onResume() {
        System.out.println("MainActivity.onResume");
        super.onResume();

        if (renderer != null) {
            glSurfaceView.onResume();
            Audio.reInit();
        }
    }

    public void showAd() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mAd.isLoaded()) {
                    mAd.show();
                } else {
                    showText("Ad is not loaded");
                }
            }
        });
    }

    public void showAdNotReady() {
        runOnUiThread(new Runnable() {
           public void run() {
               showText(adError);
           }
        });
    }

    public void showText(final String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void loadRewardedVideoAd() {
        Game.adReady = false;
        AdRequest request = new AdRequest.Builder()
                //.addTestDevice("fasfssd") // to generate test device id in logcat
                //.addTestDevice("398392EE9C6EEC32D61EFDB68EDE8C7C") // genymotion emulator
                //.addTestDevice("35515BE4990D8E2E2BF9922A5877B7C8") // Xiaomi redmi note 3
                .build();

        if (request.isTestDevice(this)) {
            mAd.loadAd("ca-app-pub-1002179312870743/4951551116", request); // solvers ad
            ///mAd.loadAd("ca-app-pub-3940256099942544/5224354917", request); // test ads from google
            //showText("loadRewarededVideoAd");
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //showText("onRewardedVideoAdLoaded");
        Game.adReady = true;
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //showText("onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        //showText("onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //showText("onRewardedVideoAdClosed");
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //showText("onRewarded! currency: " + rewardItem.getType() + ",  amount: " + rewardItem.getAmount());
        Game.incrementSolverCount();
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //showText("onRewardedVideoAdLeftApplication");
        // what to do here???
        // do nothing!?
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        //showText("onRewardedVideoAdFailedToLoad");
        switch (errorCode) {
            case ERROR_CODE_INTERNAL_ERROR:
                adError = "Unable to display Ad\nPlease try again later";
                break;

            case ERROR_CODE_INVALID_REQUEST:
                adError = "Invalid request";
                break;

            case ERROR_CODE_NETWORK_ERROR:
                adError = "Network error, please check your connection";
                break;

            case ERROR_CODE_NO_FILL:
                adError = "No fill, please try again later.";
                break;
        }
        loadRewardedVideoAd();
    }

}
