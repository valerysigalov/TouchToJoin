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
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SnoozeAlarm extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        int notificationId = extras.getInt("notificationId");
        String snooze = SettingsActivity.getValue("snooze");
        int minutes = Integer.parseInt(snooze);
        boolean isVisible = extras.getBoolean("visible");
        DebugLog.writeLog("SnoozeAlarm: snooze notification with Id " + notificationId + " for " + minutes + " minutes.");

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationId);

        Intent intent = new Intent(this, SendAlarm.class);
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000/*minutes*DateUtils.MINUTE_IN_MILLIS*/, pendingIntent);

        if (isVisible == false) {
            DebugLog.writeLog("SnoozeAlarm: return application to background");
            moveTaskToBack(true);
        }

        finish();
    }
}
