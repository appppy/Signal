package jp.osaka.appppy.signal;

import android.os.CountDownTimer;
import android.os.Handler;

/**
 * Created by appy_000 on 2015/07/20.
 */
public class PedestrianSignalController {

    private CountDownTimer mRedTimer;
    private CountDownTimer mBlueTimer;
    private CountDownTimer mBlinkingBlueTimer;

    private final Handler mHandler;
    private final PedestrianSignalView mView;

    public PedestrianSignalController(Handler handler, PedestrianSignalView view) {
        mHandler = handler;
        mView = view;
    }

    public void setup(int blue, int yellow, int red) {

        if (0 <= blue) {
            mBlueTimer = new CountDownTimer(blue * 1000, 100) {
                @Override
                public void onTick(long l) {
                    mView.color = SignalConstants.Color.BLUE;
                    mView.invalidate();
                }

                @Override
                public void onFinish() {
                    mHandler.post(() -> {
                        mBlueTimer.cancel();
                        if (null != mBlinkingBlueTimer) {
                            mBlinkingBlueTimer.start();
                        } else if (null != mRedTimer) {
                            mRedTimer.start();
                        } else {
                            mBlueTimer.start();
                        }
                    });
                }
            };
        } else {
            mBlueTimer = null;
        }

        if (0 <= yellow) {
            mBlinkingBlueTimer = new CountDownTimer(yellow * 1000, 300) {
                @Override
                public void onTick(long l) {
                    if (SignalConstants.Color.BLUE == mView.color) {
                        mView.color = SignalConstants.Color.BLACK;
                    } else {
                        mView.color = SignalConstants.Color.BLUE;
                    }
                    mView.invalidate();
                }

                @Override
                public void onFinish() {
                    mHandler.post(() -> {
                        mBlinkingBlueTimer.cancel();
                        if (null != mRedTimer) {
                            mRedTimer.start();
                        } else if (null != mBlueTimer) {
                            mBlueTimer.start();
                        } else {
                            mBlinkingBlueTimer.start();
                        }
                    });
                }
            };
        } else {
            mBlinkingBlueTimer = null;
        }

        if (0 <= red) {
            mRedTimer = new CountDownTimer(red * 1000, 100) {
                @Override
                public void onTick(long l) {
                    mView.color = SignalConstants.Color.RED;
                    mView.invalidate();
                }

                @Override
                public void onFinish() {
                    mHandler.post(() -> {
                        mRedTimer.cancel();
                        if (null != mBlueTimer) {
                            mBlueTimer.start();
                        } else if (null != mBlinkingBlueTimer) {
                            mBlinkingBlueTimer.start();
                        } else {
                            mRedTimer.start();
                        }
                    });
                }
            };
        } else {
            mRedTimer = null;
        }
    }

    public void start() {
        stop();
        if (null != mRedTimer) {
            start(mRedTimer);
        } else if (null != mBlueTimer) {
            start(mBlueTimer);
        } else if (null != mBlinkingBlueTimer) {
            start(mBlinkingBlueTimer);
        }
    }

    public void stop() {
        stop(mBlueTimer);
        stop(mRedTimer);
        stop(mBlinkingBlueTimer);
    }

    private void start(CountDownTimer timer) {
        if (null != timer) {
            timer.start();
        }
    }

    private void stop(CountDownTimer timer) {
        if (null != timer) {
            timer.cancel();
        }
    }


}
