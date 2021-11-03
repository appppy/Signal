
package jp.osaka.appppy.signal;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class SignalView extends View {

    public int color = 0;

    private static final String TAG = SignalView.class.getSimpleName();
    private final boolean DEBUG = false;

    private final List<Signal> mSignals = new ArrayList<>(3);

    public SignalView(Context context) {
        super(context);
        if (DEBUG) {
            Log.i(TAG, "SignalView enter.");
        }

        Display display = ((MainActivity) context).getWindowManager().getDefaultDisplay();
        int dispHeight = display.getHeight();
        int dispWidth = display.getWidth();
        int radius  = dispWidth  / 6;
        int centerX = dispWidth  / 2;
        int centerY = dispHeight / 2;

        mSignals.add(new Signal(centerX - radius * 2, centerY, radius));
        mSignals.add(new Signal(centerX, centerY, radius));
        mSignals.add(new Signal(centerX + radius * 2, centerY, radius));

        if (DEBUG) {
            Log.i(TAG, "SignalView exit.");
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DEBUG) {
            Log.i(TAG, "onDraw enter.");
        }

        Paint paint = new Paint();
        if (SignalConstants.Color.BLUE == color) {
            paint.setColor(Color.BLUE);
        } else {
            paint.setColor(Color.BLACK);
        }
        paint.setAntiAlias(true);
        canvas.drawCircle(mSignals.get(0).x, mSignals.get(0).y, mSignals.get(0).radius, paint);

        if (SignalConstants.Color.YELLOW == color) {
            paint.setColor(Color.YELLOW);
        } else {
            paint.setColor(Color.BLACK);
        }
        canvas.drawCircle(mSignals.get(1).x, mSignals.get(1).y, mSignals.get(1).radius, paint);

        if (SignalConstants.Color.RED == color) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.BLACK);
        }
        canvas.drawCircle(mSignals.get(2).x, mSignals.get(2).y, mSignals.get(2).radius, paint);

        if (DEBUG) {
            Log.i(TAG, "onDraw exit.");
        }
    }

    private static class Signal {
        int x;
        int y;
        int radius;

        Signal(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }
    }

}
