package jp.osaka.appppy.signal;

import android.os.CountDownTimer;
import android.os.Handler;

/**
 * Created by appy_000 on 2015/07/20.
 */
public class SignalController {

    private CountDownTimer mBlueTimer;
    private CountDownTimer mBlinkingBlueTimer;
    private CountDownTimer mYellowTimer;
    private CountDownTimer mBlinkingYellowTimer;
    private CountDownTimer mRedTimer;
    private CountDownTimer mBlinkingRedTimer;

    private final Handler mHandler;
    private final SignalView mView;

    public SignalController(Handler handler, SignalView view) {
        mHandler = handler;
        mView = view;
    }

    public void setup(int blue, int blinkingBlue, int yellow, int blinkingYellow, int red, int blinkingRed) {

        if (0 < blue) {
            mBlueTimer = new CountDownTimer(blue*1000, 100) {
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
                        } else if (null != mYellowTimer) {
                            mYellowTimer.start();
                        } else if (null != mBlinkingYellowTimer) {
                            mBlinkingYellowTimer.start();
                        } else if (null != mRedTimer) {
                            mRedTimer.start();
                        } else if (null != mBlinkingRedTimer) {
                            mBlinkingRedTimer.start();
                        } else {
                            mBlueTimer.start();
                        }
                    });
                }
            };
        } else {
            mBlueTimer = null;
        }

        if (0 < blinkingBlue) {
            mBlinkingBlueTimer = new CountDownTimer(blinkingBlue * 1000, 300) {
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
                        if (null != mYellowTimer) {
                            mYellowTimer.start();
                        } else if (null != mBlinkingYellowTimer) {
                            mBlinkingYellowTimer.start();
                        } else if (null != mRedTimer) {
                            mRedTimer.start();
                        } else if (null != mBlinkingRedTimer) {
                            mBlinkingRedTimer.start();
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


        if (0 < yellow) {
            mYellowTimer = new CountDownTimer(yellow * 1000, 100) {
                @Override
                public void onTick(long l) {
                    mView.color = SignalConstants.Color.YELLOW;
                    mView.invalidate();
                }

                @Override
                public void onFinish() {
                    mHandler.post(() -> {
                        mYellowTimer.cancel();
                        if (null != mBlinkingYellowTimer) {
                            mBlinkingYellowTimer.start();
                        } else if (null != mRedTimer) {
                            mRedTimer.start();
                        } else if (null != mBlinkingRedTimer) {
                            mBlinkingRedTimer.start();
                        } else if (null != mBlueTimer) {
                            mBlueTimer.start();
                        } else if (null != mBlinkingBlueTimer) {
                            mBlinkingBlueTimer.start();
                        } else {
                            mYellowTimer.start();
                        }
                    });
                }
            };
        } else {
            mYellowTimer = null;
        }

        if (0 < blinkingYellow) {
            mBlinkingYellowTimer = new CountDownTimer(blinkingYellow * 1000, 300) {
                @Override
                public void onTick(long l) {
                    if (SignalConstants.Color.YELLOW == mView.color) {
                        mView.color = SignalConstants.Color.BLACK;
                    } else {
                        mView.color = SignalConstants.Color.YELLOW;
                    }
                    mView.invalidate();
                }

                @Override
                public void onFinish() {
                    mHandler.post(() -> {
                        mBlinkingYellowTimer.cancel();
                        if (null != mRedTimer) {
                            mRedTimer.start();
                        } else if (null != mBlinkingRedTimer) {
                            mBlinkingRedTimer.start();
                        } else if (null != mBlueTimer) {
                            mBlueTimer.start();
                        } else if (null != mBlinkingBlueTimer) {
                            mBlinkingBlueTimer.start();
                        } else if (null != mYellowTimer) {
                            mYellowTimer.start();
                        } else {
                            mBlinkingYellowTimer.start();
                        }
                    });
                }
            };
        } else {
            mBlinkingYellowTimer = null;
        }

        if (0 < red) {
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
                        if (null != mBlinkingRedTimer) {
                            mBlinkingRedTimer.start();
                        } else if (null != mBlueTimer) {
                            mBlueTimer.start();
                        } else if (null != mBlinkingBlueTimer) {
                            mBlinkingBlueTimer.start();
                        } else if (null != mYellowTimer) {
                            mYellowTimer.start();
                        } else if (null != mBlinkingYellowTimer) {
                            mBlinkingYellowTimer.start();
                        } else {
                            mRedTimer.start();
                        }
                    });
                }
            };
        } else {
            mRedTimer = null;
        }

        if (0 < blinkingRed) {
            mBlinkingRedTimer = new CountDownTimer(blinkingRed * 1000, 300) {
                @Override
                public void onTick(long l) {
                    if (SignalConstants.Color.RED == mView.color) {
                        mView.color = SignalConstants.Color.BLACK;
                    } else {
                        mView.color = SignalConstants.Color.RED;
                    }
                    mView.invalidate();
                }

                @Override
                public void onFinish() {
                    mHandler.post(() -> {
                        mBlinkingRedTimer.cancel();
                        if (null != mBlueTimer) {
                            mBlueTimer.start();
                        } else if (null != mBlinkingBlueTimer) {
                            mBlinkingBlueTimer.start();
                        } else if (null != mYellowTimer) {
                            mYellowTimer.start();
                        } else if (null != mBlinkingYellowTimer) {
                            mBlinkingYellowTimer.start();
                        } else if (null != mRedTimer) {
                            mRedTimer.start();
                        } else {
                            mBlinkingRedTimer.start();
                        }
                    });
                }
            };
        } else {
            mBlinkingRedTimer = null;
        }

    }

    public void start() {
        stop();
        if (null != mBlueTimer) {
            start(mBlueTimer);
        } else if (null != mBlinkingBlueTimer) {
            start(mBlinkingBlueTimer);
        } else if (null != mYellowTimer) {
            start(mYellowTimer);
        } else if (null != mBlinkingYellowTimer) {
            start(mBlinkingYellowTimer);
        } else if (null != mRedTimer) {
            start(mRedTimer);
        } else if (null != mBlinkingRedTimer) {
            start(mBlinkingRedTimer);
        }
    }

    public void stop() {
        stop(mBlueTimer);
        stop(mBlinkingBlueTimer);
        stop(mYellowTimer);
        stop(mBlinkingYellowTimer);
        stop(mRedTimer);
        stop(mBlinkingRedTimer);
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
