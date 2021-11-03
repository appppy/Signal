package jp.osaka.appppy.signal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import jp.osaka.appppy.signal.util.SystemUiHider;

public class MainActivity extends FragmentActivity {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private CountDownTimer mCountDownTimer;
    private SignalView mSignalView;
    private PedestrianSignalView mPedestrianSignalView;
    private boolean isFinished = false;

    private SignalController mController;
    private PedestrianSignalController mPedestrianController;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("INTERSTITIAL_AD_KEY", isFinished);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isFinished = savedInstanceState.getBoolean("INTERSTITIAL_AD_KEY");
    }

    @SuppressLint({"SwitchIntDef", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        @SuppressLint("CutPasteId") final View contentView = findViewById(R.id.fullscreen_content);
        @SuppressLint("CutPasteId") final FrameLayout frameLayout = findViewById(R.id.fullscreen_content);

        mSignalView = new SignalView(this);
        frameLayout.addView(mSignalView);
        mPedestrianSignalView = new PedestrianSignalView(this);
        frameLayout.addView(mPedestrianSignalView);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        switch(config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                //縦方向
                mPedestrianSignalView.setVisibility(View.VISIBLE);
                mSignalView.setVisibility(View.INVISIBLE);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
            default:
                //横方向
                mPedestrianSignalView.setVisibility(View.INVISIBLE);
                mSignalView.setVisibility(View.VISIBLE);
                break;
        }

        // ロックオフ
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCountDownTimer = new CountDownTimer(3000, 50) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                isFinished = true;
            }
        };

        mController = new SignalController(new Handler(), mSignalView);
        mPedestrianController = new PedestrianSignalController(new Handler(), mPedestrianSignalView);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        // If the ViewPropertyAnimator API is available
                        // (Honeycomb MR2 and later), use it to animate the
                        // in-layout UI controls at the bottom of the
                        // screen.
                        if (mControlsHeight == 0) {
                            mControlsHeight = controlsView.getHeight();
                        }
                        if (mShortAnimTime == 0) {
                            mShortAnimTime = getResources().getInteger(
                                    android.R.integer.config_shortAnimTime);
                        }

                        controlsView.animate()
                                .translationY(visible ? 0 : mControlsHeight)
                                .setDuration(mShortAnimTime);

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        final View dummy = findViewById(R.id.dummy_button);
        dummy.setOnTouchListener((v, event) -> {
            startActivity(new Intent(this, SettingsActivity.class));
            return false;
        });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(view -> {
            if (TOGGLE_ON_CLICK) {
                mSystemUiHider.toggle();
            } else {
                mSystemUiHider.show();
            }
        });
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        switch (config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                //縦方向
                mPedestrianSignalView.setVisibility(View.VISIBLE);
                mSignalView.setVisibility(View.INVISIBLE);
                if (null != mPedestrianController) {
                    SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
                    int blue = pref.getInt(getString(R.string.blue_key), SignalConstants.Default.BLUE);
                    int yellow = pref.getInt(getString(R.string.yellow_key),  SignalConstants.Default.YELLOW);
                    int red = pref.getInt(getString(R.string.red),  SignalConstants.Default.RED);
                    mPedestrianController.setup(blue, yellow, red);
                    mPedestrianController.start();
                }
                if (null != mController) {
                    mController.stop();
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
            default:
                //横方向
                mPedestrianSignalView.setVisibility(View.INVISIBLE);
                mSignalView.setVisibility(View.VISIBLE);
                if (null != mPedestrianController) {
                    mPedestrianController.stop();
                }
                if (null != mController) {
                    SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
                    int blue = pref.getInt(getString(R.string.blue_key), SignalConstants.Default.BLUE);
                    int yellow = pref.getInt(getString(R.string.yellow_key),  SignalConstants.Default.YELLOW);
                    int red = pref.getInt(getString(R.string.red),  SignalConstants.Default.RED);
                    int blinkingBlue = pref.getInt(getString(R.string.blinking_blue), SignalConstants.Default.BLUE_BLINK);
                    int blinkingYellow = pref.getInt(getString(R.string.blinking_yellow_key), SignalConstants.Default.YELLOW_BLINK);
                    int blinkingRed = pref.getInt(getString(R.string.blinking_red), SignalConstants.Default.RED_BLINK);
                    mController.setup(blue, blinkingBlue, yellow, blinkingYellow, red, blinkingRed);
                    mController.start();
                }
                break;
            case Configuration.ORIENTATION_UNDEFINED:
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    @SuppressLint("SwitchIntDef")
    @Override
    public void onResume() {
        super.onResume();
        if(!isFinished) {
            mCountDownTimer.start();
        }

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        switch(config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                //縦方向
                mPedestrianSignalView.setVisibility(View.VISIBLE);
                mSignalView.setVisibility(View.INVISIBLE);
                if (null != mPedestrianController) {
                    SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
                    int blue = pref.getInt(getString(R.string.blue_key), SignalConstants.Default.BLUE);
                    int yellow = pref.getInt(getString(R.string.yellow_key),  SignalConstants.Default.YELLOW);
                    int red = pref.getInt(getString(R.string.red),  SignalConstants.Default.RED);
                    mPedestrianController.setup(blue, yellow, red);
                    mPedestrianController.start();
                }
                if (null != mController) {
                    mController.stop();
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
            default:
                //横方向
                mPedestrianSignalView.setVisibility(View.INVISIBLE);
                mSignalView.setVisibility(View.VISIBLE);
                if (null != mPedestrianController) {
                    mPedestrianController.stop();
                }
                if (null != mController) {
                    SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
                    int blue = pref.getInt(getString(R.string.blue_key), SignalConstants.Default.BLUE);
                    int yellow = pref.getInt(getString(R.string.yellow_key),  SignalConstants.Default.YELLOW);
                    int red = pref.getInt(getString(R.string.red),  SignalConstants.Default.RED);
                    int blinkingBlue = pref.getInt(getString(R.string.blinking_blue), SignalConstants.Default.BLUE_BLINK);
                    int blinkingYellow = pref.getInt(getString(R.string.blinking_yellow_key), SignalConstants.Default.YELLOW_BLINK);
                    int blinkingRed = pref.getInt(getString(R.string.blinking_red), SignalConstants.Default.RED_BLINK);
                    mController.setup(blue, blinkingBlue, yellow, blinkingYellow, red, blinkingRed);
                    mController.start();
                }
                break;
        }
    }

    @SuppressLint("SwitchIntDef")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        switch(config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                //縦方向
                mPedestrianSignalView.setVisibility(View.VISIBLE);
                mSignalView.setVisibility(View.INVISIBLE);
                if (null != mPedestrianController) {
                    SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
                    int blue = pref.getInt(getString(R.string.blue_key), SignalConstants.Default.BLUE);
                    int yellow = pref.getInt(getString(R.string.yellow_key),  SignalConstants.Default.YELLOW);
                    int red = pref.getInt(getString(R.string.red),  SignalConstants.Default.RED);
                    mPedestrianController.setup(blue, yellow, red);
                    mPedestrianController.start();
                }
                if (null != mController) {
                    mController.stop();
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
            default:
                //横方向
                mPedestrianSignalView.setVisibility(View.INVISIBLE);
                mSignalView.setVisibility(View.VISIBLE);
                if (null != mPedestrianController) {
                    mPedestrianController.stop();
                }
                if (null != mController) {
                    SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
                    int blue = pref.getInt(getString(R.string.blue_key), SignalConstants.Default.BLUE);
                    int yellow = pref.getInt(getString(R.string.yellow_key),  SignalConstants.Default.YELLOW);
                    int red = pref.getInt(getString(R.string.red),  SignalConstants.Default.RED);
                    int blinkingBlue = pref.getInt(getString(R.string.blinking_blue), SignalConstants.Default.BLUE_BLINK);
                    int blinkingYellow = pref.getInt(getString(R.string.blinking_yellow_key), SignalConstants.Default.YELLOW_BLINK);
                    int blinkingRed = pref.getInt(getString(R.string.blinking_red), SignalConstants.Default.RED_BLINK);
                    mController.setup(blue, blinkingBlue, yellow, blinkingYellow, red, blinkingRed);
                    mController.start();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        mCountDownTimer.cancel();
        mController.stop();
        mPedestrianController.stop();
        super.onPause();
    }
}
