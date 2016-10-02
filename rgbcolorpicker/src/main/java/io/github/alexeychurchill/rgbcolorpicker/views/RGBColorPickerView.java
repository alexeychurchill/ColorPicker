package io.github.alexeychurchill.rgbcolorpicker.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import io.github.alexeychurchill.rgbcolorpicker.utils.ColorPickerUtils;
import io.github.alexeychurchill.rgbcolorpicker.R;

/**
 * RGB Color Picker view
 * Color picker, that allows to choose colors
 * with 3 seekbars for red, green and blue color's
 * components.
 * Has callback interface, which calls everytime
 * color was changed.
 */

public class RGBColorPickerView extends LinearLayout {
    //Color's components, initial - black
    private int mRed = 127;
    private int mGreen = 127;
    private int mBlue = 127;
    //View
    private View mRootView;
    private boolean mShowWebColor = true;
    //Listener
    private OnColorChangeListener mListener;
    /**
     * SeekBars' listener
     * Handles events only from user (i.e. manual changing seekbars' values)
     */
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            if (progress > 255) {
                progress = 255;
            }
            if (progress < 0) {
                progress = 0;
            }
            if (R.id.sbRed == seekBar.getId()) { //Red component
                mRed = progress;
            }
            if (R.id.sbGreen == seekBar.getId()) { //Green component
                mGreen = progress;
            }
            if (R.id.sbBlue == seekBar.getId()) { //Blue component
                mBlue = progress;
            }
            updateColorValuesLabels();
            updateWebColorValue();
            updateSwatch();
            //Calling listener
            if (mListener != null) {
                mListener.onColorChanged(RGBColorPickerView.this, Color.rgb(mRed, mGreen, mBlue), true);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { //Ignore
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { //Ignore
        }
    };

    public RGBColorPickerView(Context context) {
        super(context);
        init();
    }

    public RGBColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInitialValues(context, attrs);
        init();
    }

    /**
     * Gets initial values from XML attributes
     * @param context Context
     * @param attrs Attributes set
     */
    private void setInitialValues(Context context, AttributeSet attrs) {
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.RGBColorPickerView);
        //Getting initial color
        int color = attrsArray.getColor(R.styleable.RGBColorPickerView_initialColorValue, Color.rgb(mRed, mGreen, mBlue));
        mRed = Color.red(color);
        mGreen = Color.green(color);
        mBlue = Color.blue(color);
        //Show web-color or not
        mShowWebColor = attrsArray.getBoolean(R.styleable.RGBColorPickerView_showWebColor, mShowWebColor);
        attrsArray.recycle();
    }

    /**
     * Sets showing of the web-color
     * @param show Is needed to show web-color
     */
    public void setShowWebColor(boolean show) {
        mShowWebColor = show;
        if (mRootView == null) {
            return;
        }
        FrameLayout flWebColor = ((FrameLayout) mRootView.findViewById(R.id.flWebColor));
        flWebColor.setVisibility((show) ? View.VISIBLE : View.GONE);
    }

    /**
     * Gets color
     * @return Color as integer (as same as Color.rgb())
     */
    public int getColor() {
        return Color.rgb(mRed, mGreen, mBlue);
    }

    /**
     * Sets color
     * Calls OnColorChangeListener with fromUser = false
     * @param color Color as integer (as same as Color.rgb())
     */
    public void setColor(int color) {
        mRed = Color.red(color);
        mGreen = Color.green(color);
        mBlue = Color.blue(color);
        updateSeekBars();
        updateColorValuesLabels();
        updateWebColorValue();
        updateSwatch();
        if (mListener != null) {
            mListener.onColorChanged(this, Color.rgb(mRed, mGreen, mBlue), false);
        }
    }

    /**
     * Sets listener on color change
     * @param listener Color change notification listener
     */
    public void setListener(OnColorChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * Inits view: sets seekbars' listener, updates view;
     */
    private void init() {
        mRootView = inflate(getContext(), R.layout.view_picker, this); //Inflate me
        initRedValueSeekBar(); //Set mListener
        initGreenValueSeekBar();
        initBlueValueSeekBar();
        updateSeekBars(); //Update view
        updateColorValuesLabels();
        updateWebColorValue();
        updateSwatch();
    }

    private void initRedValueSeekBar() {
        if (mRootView == null) {
            return;
        }
        SeekBar sbRed = ((SeekBar) mRootView.findViewById(R.id.sbRed));
        if (sbRed != null) {
            sbRed.setOnSeekBarChangeListener(mSeekBarChangeListener);
        }
    }

    private void initGreenValueSeekBar() {
        if (mRootView == null) {
            return;
        }
        SeekBar sbGreen = ((SeekBar) mRootView.findViewById(R.id.sbGreen));
        if (sbGreen != null) {
            sbGreen.setOnSeekBarChangeListener(mSeekBarChangeListener);
        }
    }

    private void initBlueValueSeekBar() {
        if (mRootView == null) {
            return;
        }
        SeekBar sbBlue = ((SeekBar) mRootView.findViewById(R.id.sbBlue));
        if (sbBlue != null) {
            sbBlue.setOnSeekBarChangeListener(mSeekBarChangeListener);
        }
    }

    /**
     * Updates all seekbars
     * Used by setColor()
     */
    private void updateSeekBars() {
        updateRedValueSeekBar();
        updateGreenValueSeekBar();
        updateBlueValueSeekBar();
    }

    private void updateRedValueSeekBar() {
        if (mRootView == null) {
            return;
        }
        SeekBar sbRed = ((SeekBar) mRootView.findViewById(R.id.sbRed));
        if (sbRed != null) {
            sbRed.setProgress(mRed);
        }
    }

    private void updateGreenValueSeekBar() {
        if (mRootView == null) {
            return;
        }
        SeekBar sbGreen = ((SeekBar) mRootView.findViewById(R.id.sbGreen));
        if (sbGreen != null) {
            sbGreen.setProgress(mGreen);
        }
    }

    private void updateBlueValueSeekBar() {
        if (mRootView == null) {
            return;
        }
        SeekBar sbBlue = ((SeekBar) mRootView.findViewById(R.id.sbBlue));
        if (sbBlue != null) {
            sbBlue.setProgress(mBlue);
        }
    }

    /**
     * Updates colors labels
     */
    private void updateColorValuesLabels() {
        updateRedValueLabel();
        updateGreenValueLabel();
        updateBlueValueLabel();
    }

    private void updateRedValueLabel() {
        if (mRootView == null) {
            return;
        }
        TextView tvRedValue = ((TextView) findViewById(R.id.tvRedValue));
        if (tvRedValue != null) {
            tvRedValue.setText(String.valueOf(mRed));
        }
    }

    private void updateGreenValueLabel() {
        if (mRootView == null) {
            return;
        }
        TextView tvGreenValue = ((TextView) findViewById(R.id.tvGreenValue));
        if (tvGreenValue != null) {
            tvGreenValue.setText(String.valueOf(mGreen));
        }
    }

    private void updateBlueValueLabel() {
        if (mRootView == null) {
            return;
        }
        TextView tvBlueValue = ((TextView) findViewById(R.id.tvBlueValue));
        if (tvBlueValue != null) {
            tvBlueValue.setText(String.valueOf(mBlue));
        }
    }

    /**
     * Updates color swatch
     */
    private void updateSwatch() {
        if (mRootView == null) {
            return;
        }
        LinearLayout llColorSwatch = ((LinearLayout) findViewById(R.id.llColorSwatch));
        if (llColorSwatch != null) {
            llColorSwatch.setBackgroundColor(Color.rgb(mRed, mGreen, mBlue));
        }
    }

    /**
     * If showing of the web-color turned on
     * it updates web-color value TextView
     */
    private void updateWebColorValue() {
        if (!mShowWebColor) {
            return;
        }
        if (mRootView == null) {
            return;
        }
        TextView etWebColorValue = ((TextView) mRootView.findViewById(R.id.tvWebColorValue));
        if (etWebColorValue != null) {
            etWebColorValue.setText(ColorPickerUtils.toWebColor(Color.rgb(mRed, mGreen, mBlue)));
        }
    }

    /**
     * Color change listener interface
     */
    public interface OnColorChangeListener {
        /**
         * OnColorChanged called when color changed by user or programmatically
         * @param colorPickerView Contains view, which called this
         * @param color New color
         * @param fromUser Equals true when changed by user with seekbars
         *                 and equals false when RGBColorPickerView.setColor() called
         */
        void onColorChanged(RGBColorPickerView colorPickerView, int color, boolean fromUser);
    }
}
