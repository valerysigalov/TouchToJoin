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
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.CalendarContract;

public class MainActivity extends Activity {

    private boolean wasRegistered = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!wasRegistered) {
            DebugLog.writeLog("Register calendar events receiver.");
            IntentFilter intentFilter = new IntentFilter(CalendarContract.ACTION_EVENT_REMINDER);
            intentFilter.addDataScheme("content");
            registerReceiver(new EventReceiver(), intentFilter);
            wasRegistered = true;
        }

        DebugLog.writeLog("Minimize main activity.");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
