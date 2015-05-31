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

package com.app.touch2join;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class REDIALog extends Activity {

    private Bundle extras;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.redialog);
        setFinishOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width  = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        setTitle(getResources().getString(R.string.disconnected));

        context = this;
        extras = getIntent().getExtras();
        String begin = extras.getString("begin").trim();
        String end = extras.getString("end").trim();
        String title = extras.getString("title").trim();
        String number = extras.getString("number").trim();
        String pin = extras.getString("pin").trim();

        TextView textView = (TextView) findViewById(R.id.message);
        String message = title + "\n" + begin.replaceAll("[^0-9:]", "").trim() +
                " \u2013 " + end + "\n" + number + "x" + pin + "#";
        textView.setText(message);

        Button rejoin = (Button) findViewById(R.id.rejoin);
        rejoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String className = "RD";
                Intent join = new Intent(Intent.ACTION_CALL);
                String number = extras.getString("number").trim();
                String pin = extras.getString("pin").trim();
                CallListener.setActiveCall(number);
                number = "tel:" + number.replaceAll("\\D", "") + Preferences.setDelay(context) + pin.replaceAll("\\D", "") + "#";
                DebugLog.writeLog(className, "rejoin call " + number);
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
