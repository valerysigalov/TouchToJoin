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

package com.app.jointhemeeting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DatePicker extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;
    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private DatePickerListener datePickerListener;
    public interface DatePickerListener {
        public void onDateSet(int year, int month, int day);
    }

    public void setDatePickerListener(DatePickerListener listener) {
        this.datePickerListener = listener;
    }

    protected void notifyDatePickerListener(int year, int month, int day) {
        if(this.datePickerListener != null) {
            this.datePickerListener.onDateSet(year, month, day);
        }
    }

    public static DatePicker newInstance(DatePickerListener listener, int year, int month, int day) {
        DatePicker fragment = new DatePicker();
        fragment.setDatePickerListener(listener);
        fragment.setDate(year, month, day);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        DebugLog.writeLog("Dialog date: " + month + "-" + day + "-" + year);

        // Call the listener and pass the date back to it.
        notifyDatePickerListener(year, month, day);
    }
}
