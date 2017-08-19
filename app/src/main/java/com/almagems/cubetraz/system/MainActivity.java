package com.almagems.cubetraz.system;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.almagems.cubetraz.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public final class MainActivity extends Activity implements RewardedVideoAdListener {
    private RewardedVideoAd mAd;

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
                                renderer.handleTouchPress(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchDrag(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_UP) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchRelease(x, y);
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
            renderer.pause();
        }
    }

    @Override
    protected void onResume() {
        System.out.println("MainActivity.onResume");
        super.onResume();

        if (renderer != null) {
            glSurfaceView.onResume();
            renderer.resume();
        }
    }

    public void showAd() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mAd.isLoaded()) {
                    mAd.show();
                } else {
                    System.out.println("ad is NOT loaded yet");
                }
            }
        });
    }

    private void loadRewardedVideoAd() {
        final String adUnitRewardSolver = "ca-app-pub-1002179312870743/4951551116";

        AdRequest request = new AdRequest.Builder()
                //.addTestDevice("fasfssd") // to generate test device id in logcat
                //.addTestDevice("398392EE9C6EEC32D61EFDB68EDE8C7C") // genymotion emulator
                .addTestDevice("35515BE4990D8E2E2BF9922A5877B7C8")
                .build();

        if (request.isTestDevice(this)) {
        //if (true) {
            Toast.makeText(this, "TEST DEVICE", Toast.LENGTH_SHORT).show();
            mAd.loadAd(adUnitRewardSolver, request);
            ///mAd.loadAd("ca-app-pub-3940256099942544/5224354917", request);
            Toast.makeText(this, "loadRewarededVideoAd", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NOT A TEST DEVICE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                rewardItem.getAmount(), Toast.LENGTH_SHORT).show();

        renderer.incrementSolverCount();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
        // what to do here???
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        loadRewardedVideoAd();
    }
}
