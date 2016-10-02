package io.github.alexeychurchill.rgbcolorpicker.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import io.github.alexeychurchill.rgbcolorpicker.R;
import io.github.alexeychurchill.rgbcolorpicker.views.RGBColorPickerView;

/**
 * RGB Color picker dialog
 */

public class RGBColorPickerDialog extends DialogFragment {
    private OnColorChosenListener mColorChosenListener;
    private OnColorCancelListener mColorCancelListener;
    private RGBColorPickerView mRgbColorPickerView;
    private int mColor = 0;

    private RGBColorPickerView.OnColorChangeListener onColorChangeListener =
            new RGBColorPickerView.OnColorChangeListener() {
        @Override
        public void onColorChanged(RGBColorPickerView colorPickerView, int color, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            mColor = color;
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (mRgbColorPickerView == null) {
            mRgbColorPickerView = new RGBColorPickerView(getContext());
            mRgbColorPickerView.setListener(onColorChangeListener);
        }
        mRgbColorPickerView.setColor(mColor);
        return builder.setTitle(R.string.dialog_title)
                .setView(mRgbColorPickerView)
                .setPositiveButton(R.string.dialog_choose_confirm_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mColorChosenListener != null) {
                            mColorChosenListener.onColorChosen(RGBColorPickerDialog.this, mColor);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_choose_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mColorCancelListener != null) {
                            mColorCancelListener.onColorCancel(RGBColorPickerDialog.this);
                        }
                    }
                })
                .create();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        if (mRgbColorPickerView != null) {
            mRgbColorPickerView.setColor(color);
        }
    }

    public void setColorChosenListener(OnColorChosenListener colorChosenListener) {
        this.mColorChosenListener = colorChosenListener;
    }

    public void setColorCancelListener(OnColorCancelListener colorCancelListener) {
        this.mColorCancelListener = colorCancelListener;
    }

    public interface OnColorChosenListener {
        void onColorChosen(DialogFragment dialog, int color);
    }

    public interface OnColorCancelListener {
        void onColorCancel(DialogFragment dialog);
    }
}
