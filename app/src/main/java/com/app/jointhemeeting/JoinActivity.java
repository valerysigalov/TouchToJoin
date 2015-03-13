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

import java.util.ArrayList;

public class JoinActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String date = extras.getString("date");
        String title = extras.getString("title");
        String phoneNumber = extras.getString("phoneNumber");
        String pinCode = extras.getString("pinCode");

        String conf = date.trim() + " " + title.trim();
        String number = "tel:" + phoneNumber.trim() + ",,," + pinCode.trim() + "#";
        DebugLog.writeLog("JoinActivity: conference title is " + conf);
        DebugLog.writeLog("JoinActivity: conference number is " + number);

        String fileName = System.getProperty("java.io.tmpdir") + "/history.log";
        ArrayList<String> arrayList = StreamIO.ReadFile(getBaseContext(), fileName);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        arrayList.add(conf + number);
        StreamIO.WriteFile(getBaseContext(), fileName, arrayList);

        Intent join = new Intent(Intent.ACTION_CALL);
        join.setData(Uri.parse(number));
        join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(join);
        finish();
    }
}
