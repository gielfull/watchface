package com.casual_dev.CASUALWatch.widget;
/*Copyright 2014 Adam Outler

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.casual_dev.CASUALWatch.R;

public class MyTextView extends TextView {
    // Minimum text size for this text view
    public static final float MIN_TEXT_SIZE = 50;
    // Lower bounds for text size
    private float mMinTextSize = MIN_TEXT_SIZE;
    private static final String TAG = "TextView";
    // Our ellipse string
    private static final String mEllipsis = "...";

    // Registered resize listener
    private OnTextResizeListener mTextResizeListener;

    // Flag for text and/or size changes to force a resize
    private boolean mNeedsResize = false;

    // Text size that is set from code. This acts as a starting point for resizing
    private float mTextSize;

    // Temporary upper bounds on the starting text size
    private float mMaxTextSize = 0;
    // Text view line spacing multiplier
    private float mSpacingMult = 1.0f;
    // Text view additional line spacing
    private float mSpacingAdd = 0.0f;
    // Add ellipsis to text that overflows at the smallest text size
    private boolean mAddEllipsis = true;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        if (!getRootView().isInEditMode()) setCustomFont(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTextSize = getTextSize();
        if (!getRootView().isInEditMode()) setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        String customFont = "fonts/" + a.getString(R.styleable.MyTextView_fontName);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + asset + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

    /**
     * When text changes, set the force resize flag to true and reset the text size.
     */
    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        mNeedsResize = true;
        // Since this view may be reused, it is good to reset the text size
        resetTextSize();
    }

    /**
     * If the text view size changed, set the force resize flag to true
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mNeedsResize = true;
        }
    }

    /**
     * Register listener to receive resize notifications
     *
     * @param listener listener to use
     */
    @SuppressWarnings("unused")
    public void setOnResizeListener(OnTextResizeListener listener) {
        mTextResizeListener = listener;
    }

    /**
     * Override the set text size to update our internal reference values
     */
    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        mTextSize = getTextSize();
    }

    /**
     * Override the set text size to update our internal reference values
     */
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        mTextSize = getTextSize();
    }

    /**
     * Override the set line spacing to update our internal reference values
     */
    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mSpacingMult = mult;
        mSpacingAdd = add;
    }

    /**
     * Return upper text size limit
     *
     * @return max text size
     */
    @SuppressWarnings("unused")
    public float getMaxTextSize() {
        return mMaxTextSize;
    }

    /**
     * Set the upper text size limit and invalidate the view
     *
     * @param maxTextSize max size
     */
    @SuppressWarnings("unused")

    public void setMaxTextSize(float maxTextSize) {
        mMaxTextSize = maxTextSize;
        requestLayout();
        invalidate();
    }

    /**
     * Return lower text size limit
     *
     * @return minimum text size
     */
    @SuppressWarnings("unused")

    public float getMinTextSize() {
        return mMinTextSize;
    }

    /**
     * Set the lower text size limit and invalidate the view
     *
     * @param minTextSize minimum text size
     */
    @SuppressWarnings("unused")

    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
        requestLayout();
        invalidate();
    }

    /**
     * Return flag to add ellipsis to text that overflows at the smallest text size
     *
     * @return ellipsis string
     */
    @SuppressWarnings("unused")
    public boolean getAddEllipsis() {
        return mAddEllipsis;
    }

    /**
     * Set flag to add ellipsis to text that overflows at the smallest text size
     *
     * @param addEllipsis true if ellipsis is to be added
     */
    @SuppressWarnings("unused")
    public void setAddEllipsis(boolean addEllipsis) {
        mAddEllipsis = addEllipsis;
    }

    /**
     * Reset the text to the original size
     */
    public void resetTextSize() {
        if (mTextSize > 0) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mMaxTextSize = mTextSize;
        }
    }

    /**
     * Resize text after measuring
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed || mNeedsResize) {
            int widthLimit = (right - left) - getCompoundPaddingLeft() - getCompoundPaddingRight();
            int heightLimit = (bottom - top) - getCompoundPaddingBottom() - getCompoundPaddingTop();
            resizeText(widthLimit, heightLimit);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * Resize the text size with default width and height
     */
    @SuppressWarnings("unused")
    public void resizeText() {

        int heightLimit = getHeight() - getPaddingBottom() - getPaddingTop();
        int widthLimit = getWidth() - getPaddingLeft() - getPaddingRight();
        resizeText(widthLimit, heightLimit);
    }

    /**
     * Resize the text size with specified width and height
     *
     * @param width  width of unit
     * @param height height of unit
     */
    public void resizeText(int width, int height) {
        CharSequence text = getText();
        // Do not resize if the view does not have dimensions or there is no text
        if (text == null || text.length() == 0 || height <= 0 || width <= 0 || mTextSize == 0) {
            return;
        }

        // Get the text view's paint object
        TextPaint textPaint = getPaint();

        // Store the current text size
        float oldTextSize = textPaint.getTextSize();
        // If there is a max text size set, use the lesser of that and the default text size
        float targetTextSize = mMaxTextSize > 0 ? Math.min(mTextSize, mMaxTextSize) : mTextSize;

        // Get the required text height
        int textHeight = getTextHeight(text, textPaint, width, targetTextSize);

        // Until we either fit within our text view or we had reached our min text size, incrementally try smaller sizes
        while (textHeight > height && targetTextSize > mMinTextSize) {
            targetTextSize = Math.max(targetTextSize - 2, mMinTextSize);
            textHeight = getTextHeight(text, textPaint, width, targetTextSize);
        }

        // If we had reached our minimum text size and still don't fit, append an ellipsis
        if (mAddEllipsis && targetTextSize == mMinTextSize && textHeight > height) {
            // Draw using a static layout
            // modified: use a copy of TextPaint for measuring
            TextPaint paint = new TextPaint(textPaint);
            // Draw using a static layout
            StaticLayout layout = new StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, false);
            // Check that we have a least one line of rendered text
            if (layout.getLineCount() > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line
                int lastLine = layout.getLineForVertical(height) - 1;
                // If the text would not even fit on a single line, clear it
                if (lastLine < 0) {
                    setText("");
                }
                // Otherwise, trim to the previous line and add an ellipsis
                else {
                    int start = layout.getLineStart(lastLine);
                    int end = layout.getLineEnd(lastLine);
                    float lineWidth = layout.getLineWidth(lastLine);
                    float ellipseWidth = textPaint.measureText(mEllipsis);

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (width < lineWidth + ellipseWidth) {
                        lineWidth = textPaint.measureText(text.subSequence(start, --end + 1).toString());
                    }
                    setText(text.subSequence(0, end) + mEllipsis);
                }
            }
        }

        // Some devices try to auto adjust line spacing, so force default line spacing
        // and invalidate the layout as a side effect
        setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
        setLineSpacing(mSpacingAdd, mSpacingMult);

        // Notify the listener if registered
        if (mTextResizeListener != null) {
            mTextResizeListener.onTextResize(this, oldTextSize, targetTextSize);
        }

        // Reset force resize flag
        mNeedsResize = false;
    }

    // Set the text size of the text paint object and use a static layout to render text off screen before measuring
    private int getTextHeight(CharSequence source, TextPaint paint, int width, float textSize) {
        // modified: make a copy of the original TextPaint object for measuring
        // (apparently the object gets modified while measuring, see also the
        // docs for TextView.getPaint() (which states to access it read-only)
        TextPaint paintCopy = new TextPaint(paint);
        // Update the text paint object
        paintCopy.setTextSize(textSize);
        // Measure using a static layout
        StaticLayout layout = new StaticLayout(source, paintCopy, width, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);
        return layout.getHeight();
    }

    // Interface for resize notifications
    public interface OnTextResizeListener {
        public void onTextResize(TextView textView, float oldSize, float newSize);
    }

}