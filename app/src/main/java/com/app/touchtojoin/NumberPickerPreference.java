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
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

class NumberPickerPreference extends DialogPreference {

    private final Context context;
    private NumberPicker picker;
    private Integer curValue;
    Integer minValue;
    Integer maxValue;
    Integer defValue;
    Integer stepValue;
    String attrName;
    String timeUnit;

    NumberPickerPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
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
    protected void onBindDialogView(@NonNull View view) {

        super.onBindDialogView(view);
        setDisplayedValues();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        if (positiveResult) {
            setValue(minValue+picker.getValue()*stepValue);
        }
    }

    void onSetInitialValue() {

        Integer value = Preferences.getInt(context, attrName, defValue);
        setValue(value);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        if (restorePersistedValue) {
            Integer value = Preferences.getInt(context, attrName, defValue);
            setValue(value);
        }
        else {
            setValue(defValue);
        }
    }

    private void setDisplayedValues() {

        Integer valueSetLen = (maxValue-minValue)/stepValue+1;
        String[] valueSet = new String[valueSetLen];

        for (int index = 0; index < valueSetLen; index++) {
            valueSet[index] = Integer.toString(minValue+index*stepValue);
        }
        picker.setMinValue(0);
        picker.setMaxValue(valueSetLen-1);
        picker.setDisplayedValues(valueSet);
        picker.setValue((curValue-minValue)/stepValue);
    }

    private void setValue(Integer value) {

        curValue = value;
        Preferences.putInt(context, attrName, value);
        setSummary(String.valueOf(value) + " " + timeUnit);
    }
}
