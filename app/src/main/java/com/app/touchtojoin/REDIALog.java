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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class REDIALog extends Activity {

    private final String className = "REDIALog";
    private Bundle extras;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.redialog);

        extras = getIntent().getExtras();
        String date = extras.getString("date").trim();
        DebugLog.writeLog(className, "conference date is " + date);
        String begin = extras.getString("begin").trim();
        DebugLog.writeLog(className, "conference begin time is " + begin);
        String end = extras.getString("end").trim();
        DebugLog.writeLog(className, "conference end time is " + end);
        String title = extras.getString("title").trim();
        DebugLog.writeLog(className, "conference title is " + title);
        String number = extras.getString("number").trim();
        DebugLog.writeLog(className, "conference number is " + number);
        String pin = extras.getString("pin").trim();
        DebugLog.writeLog(className, "conference pin is " + pin);

        TextView textView = (TextView) findViewById(R.id.message);
        String message = "The conference \"" + title + "\" will end at " + end + ".\n" +
                "Would you like to rejoin the call at " + number + "x" + pin + "#?";
        textView.setText(message);

        Button rejoin = (Button) findViewById(R.id.rejoin);
        rejoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent join = new Intent(Intent.ACTION_CALL);
                String number = "tel:" + extras.getString("number").trim() + ",,," +
                        extras.getString("pin").trim() + "#";
                DebugLog.writeLog(className, "dialing number " + number);
                join.setData(Uri.parse(number));
                join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(join);
                finish();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
            }
        });
    }
}
