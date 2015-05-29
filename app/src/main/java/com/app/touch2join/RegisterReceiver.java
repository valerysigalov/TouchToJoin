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

import android.content.Context;
import android.content.IntentFilter;
import android.provider.CalendarContract;
import android.telephony.TelephonyManager;

class RegisterReceiver {

    private static final String className = "RR";
    private static boolean wasRegistered = false;

    public static void registerReceiver(Context context) {

        if (!wasRegistered) {
            DebugLog.writeLog(className, "register");
            IntentFilter eventFilter = new IntentFilter(CalendarContract.ACTION_EVENT_REMINDER);
            eventFilter.addDataScheme("content");
            context.getApplicationContext().registerReceiver(new EventReceiver(), eventFilter);

            IntentFilter callFilter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
            context.getApplicationContext().registerReceiver(new CallListener(), callFilter);

            IntentFilter appFilter = new IntentFilter("com.app.touch2join");
            context.getApplicationContext().registerReceiver(new SendAlarm(), appFilter);
            context.getApplicationContext().registerReceiver(new PublishAlarm(), appFilter);

            wasRegistered = true;
        }
    }
}
