package com.json.basewebview.Utils.UpdateApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * 更新应用的进度条
 */

public class UpdateProgressBar extends ProgressBar {
        private static final String TAG = "SimpleProgressbar";

        public static final int DEFAULT_UNREACHED_COLOR = 0xFFFFFFFF;
        public static final int DEFAULT_REACHED_COLOR = 0xFF53D067;
        public static final int DEFAULT_TEXT_COLOR = 0xFF0000CD;
        public static final String DEFAULT_TEXT = "100%";
        // 进度条默认高，单位为 dp
        public static final int DEFAULT_LINE_HEIGHT = 2;
        // 进度条默认宽，单位为 dp
        public static final int DEFAULT_LINE_WIDTH = 100;
        // 文本大小，单位为 sp
        public static final int DEFAULT_TEXT_SIZE = 12;

        private Paint paint;
        private Rect textBound;

        private int reachedColor;
        private int unreachedColor;
        private int textColor;

        private int lineHeight;
        private int minLineHeight;
        private int minLineWidth;

        private int textSize;
        private int textHeight;
        private int textWidth;

        public UpdateProgressBar(Context context) {
            //        super(context);
            this(context, null);
        }

        public UpdateProgressBar(Context context, AttributeSet attrs) {
            //        super(context, attrs);
            this(context, attrs, 0);
        }

        public UpdateProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            paint = new Paint();
            textBound = new Rect();

            unreachedColor = DEFAULT_UNREACHED_COLOR;
            reachedColor = DEFAULT_REACHED_COLOR;
            textColor = DEFAULT_TEXT_COLOR;

            minLineHeight = dp2px(DEFAULT_LINE_HEIGHT);
            minLineWidth = dp2px(DEFAULT_LINE_WIDTH);
            textSize = sp2px(DEFAULT_TEXT_SIZE);

            // 计算文本的宽和高
            paint.setTextSize(textSize);
            paint.getTextBounds(DEFAULT_TEXT, 0, DEFAULT_TEXT.length(), textBound);
            textWidth = textBound.width();
            textHeight = textBound.height();
        }

        @Override
        protected synchronized void onDraw(Canvas canvas) {
            //        super.onDraw(canvas);
            int width = getWidth();
            int height = getHeight();

            int contentWidth = width - getPaddingLeft() - getPaddingRight();
            lineHeight = height - getPaddingTop() - getPaddingBottom();
            float ratio = getProgress() * 1.0f / getMax();
            int unreachedWidth = (int) ((contentWidth - textWidth) * (1 - ratio));
            int reachedWidth = contentWidth - textWidth - unreachedWidth;

            paint.setColor(reachedColor);
            paint.setStrokeWidth(lineHeight);

            int startX = getPaddingLeft();
            int startY = height / 2;
            int stopX = getPaddingLeft() + reachedWidth;
            int stopY = height / 2;

            canvas.drawLine(startX, startY, stopX, stopY, paint);

            String currentText = getProgress() + "%";
            paint.getTextBounds(currentText, 0, currentText.length(), textBound);

            paint.setColor(textColor);
            paint.setTextSize(textSize);
            startX = getPaddingLeft() + reachedWidth + (textWidth - textBound.width()) / 2;
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            startY = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(currentText, startX, startY, paint);

            paint.setColor(unreachedColor);
            paint.setStrokeWidth(lineHeight);
            startX = getPaddingLeft() + reachedWidth + textWidth;
            startY = height / 2;
            stopX = width - getPaddingRight();
            stopY = height / 2;
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        /**
         * dp 2 px
         *
         * @param dpVal
         */
        protected int dp2px(int dpVal) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, getResources().getDisplayMetrics());
        }

        /**
         * sp 2 px
         *
         * @param spVal
         * @return
         */
        protected int sp2px(int spVal) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    spVal, getResources().getDisplayMetrics());

        }
}

