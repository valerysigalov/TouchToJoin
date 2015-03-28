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

public class JoinActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String date = extras.getString("date").trim();
        DebugLog.writeLog("JoinActivity: conference date is " + date);
        String title = extras.getString("title").trim();
        DebugLog.writeLog("JoinActivity: conference title is " + title);
        String number = extras.getString("number").trim();
        DebugLog.writeLog("JoinActivity: conference number is " + number);
        String pin = extras.getString("pin").trim();
        DebugLog.writeLog("JoinActivity: conference pin is " + pin);

        if (pin.equals("none")) {
            FileIO.Write(date + " Subject: " + title + ", Phone: " + number);
            number = "tel:" + number + "#";
        }
        else {
            FileIO.Write(date + " Subject: " + title + ", Phone: " + number + ", PIN: " + pin);
            number = "tel:" + number + SettingsActivity.setDelay() + pin + "#";
        }

        Intent join = new Intent(Intent.ACTION_CALL);
        join.setData(Uri.parse(number));
        join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(join);
        finish();
    }
}
