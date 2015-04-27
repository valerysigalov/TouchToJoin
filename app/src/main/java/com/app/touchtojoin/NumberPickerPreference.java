/*
 * Copyright (c) 2015
 * Michael Franz (misha.franz@gmail.com)
 * Valery Sigalov (valery.sigalov@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.touchtojoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

class NumberPickerPreference extends DialogPreference {

    private NumberPicker picker;
    private Integer curValue;
    Integer minValue;
    Integer maxValue;
    Integer defValue;
    String attrName;
    String timeUnit;

    NumberPickerPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {

        super.onBindDialogView(view);

        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(curValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        if (positiveResult) {
            setValue(picker.getValue());
        }
    }

    void onSetInitialValue() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (sharedPreferences.contains(attrName)) {
            Integer value = sharedPreferences.getInt(attrName, 0);
            setValue(value);
        }
        else {
            setValue(defValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        if (restorePersistedValue) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            Integer value = sharedPreferences.getInt(attrName, 0);
            setValue(value);
        }
        else {
            setValue(defValue);
        }
    }

    void setValue(Integer value) {

        curValue = value;
        String className = "NumberPickerPreference";
        DebugLog.writeLog(className, "set value - " + value + " for " + attrName);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(attrName, curValue);
        edit.commit();

        setSummary(String.valueOf(curValue) + " " + timeUnit);
    }
}
