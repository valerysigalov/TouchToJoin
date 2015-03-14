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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RangeActivity extends Activity implements DatePicker.DatePickerListener {

    private Button startButton;
    private Button endButton;

    private ListView listView;

    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;

    enum eButton {
        START,
        END
    }

    private eButton btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.range_activity);

        setCurrentDate();

        startButton = (Button) findViewById(R.id.buttonStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn = eButton.START;
                DatePicker fragment = DatePicker.newInstance(RangeActivity.this, startYear, startMonth, startDay);
                fragment.show(getFragmentManager(), "datePicker");
            }
        });

        endButton = (Button) findViewById(R.id.buttonEnd);
        endButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn = eButton.END;
                DatePicker fragment = DatePicker.newInstance(RangeActivity.this, endYear, endMonth, endDay);
                fragment.show(getFragmentManager(), "datePicker");
            }
        });

        listView = (ListView) findViewById(R.id.listView);

        Button applyButton = (Button) findViewById(R.id.buttonApply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<String> arrayList = new ArrayList<>();
                Calendar startTime = Calendar.getInstance();
                startTime.set(startYear, startMonth, startDay);
                Calendar endTime= Calendar.getInstance();
                endTime.set(endYear, endMonth, endDay);
                ReadCalendar.getEventsByDateRange(getBaseContext(), arrayList,
                        startTime.getTimeInMillis(), endTime.getTimeInMillis());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        getBaseContext(), android.R.layout.simple_list_item_single_choice, arrayList);
                listView.setAdapter(arrayAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
        });

        Button dialButton = (Button) findViewById(R.id.buttonDial);
        dialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = listView.getCheckedItemPosition();
                if (pos != AdapterView.INVALID_POSITION) {
                    String selected = listView.getAdapter().getItem(pos).toString();
                    String title = selected.substring(0, selected.indexOf("tel:"));
                    String number = selected.substring(selected.indexOf("tel:"), selected.length());
                    DebugLog.writeLog("RangeActivity: title " + title);
                    DebugLog.writeLog("RangeActivity: number " + number);
                    Intent intent = new Intent(RangeActivity.this, JoinActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("title", title);
                    extras.putString("number", number);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });
   }

    @Override
    public void onDateSet(int year, int month, int day) {
        DebugLog.writeLog("RangeActivity: date " + month + "-" + day + "-" + year);
        if(btn == eButton.START) {
            startYear = year;
            startMonth = month;
            startDay = day;

            startButton.setText(new StringBuilder()
                       .append(startMonth + 1).append("-").append(startDay).append("-")
                       .append(startYear).append(" "));
        }
        else {
            endYear = year;
            endMonth = month;
            endDay = day;

            endButton.setText(new StringBuilder()
                     .append(endMonth + 1).append("-").append(endDay).append("-")
                     .append(endYear).append(" "));
        }
    }

    // Initialize current date for both start and end dates.
    private void setCurrentDate() {

        final Calendar calendar = Calendar.getInstance();
        startYear = endYear = calendar.get(Calendar.YEAR);
        startMonth = endMonth = calendar.get(Calendar.MONTH);
        startDay = endDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
