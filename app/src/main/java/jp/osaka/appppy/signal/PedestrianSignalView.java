
package jp.osaka.appppy.signal;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PedestrianSignalView extends View {

    public int color = 0;

    private final List<Signal> mSignals = new ArrayList<>(2);

    @SuppressWarnings("deprecation")
    public PedestrianSignalView(Context context) {
        super(context);

        Display display = ((MainActivity) context).getWindowManager().getDefaultDisplay();
        int dispHeight = display.getHeight();
        int dispWidth = display.getWidth();
        int radius  = dispWidth  / 4;
        int centerX = dispWidth  / 2;
        int centerY = dispHeight / 2;

        mSignals.add(new Signal(centerX, centerY - radius * 2, radius));
        mSignals.add(new Signal(centerX, centerY + radius * 2, radius));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        if (SignalConstants.Color.RED == color) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.BLACK);
        }
        paint.setAntiAlias(true);
        canvas.drawRect(
                mSignals.get(0).x - mSignals.get(0).radius*2,
                mSignals.get(0).y - mSignals.get(0).radius*2,
                mSignals.get(0).x + mSignals.get(0).radius*2,
                mSignals.get(0).y + mSignals.get(0).radius*2, paint);

        if (SignalConstants.Color.BLUE == color) {
            paint.setColor(Color.BLUE);
        } else {
            paint.setColor(Color.BLACK);
        }
        canvas.drawRect(
                mSignals.get(1).x - mSignals.get(0).radius*2,
                mSignals.get(1).y - mSignals.get(0).radius*2,
                mSignals.get(1).x + mSignals.get(0).radius*2,
                mSignals.get(1).y + mSignals.get(0).radius*2, paint);
    }

    private static class Signal {
        int x;
        int y;
        int radius;

        Signal(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.radius = 0;
            this.radius = radius;
        }
    }

}
