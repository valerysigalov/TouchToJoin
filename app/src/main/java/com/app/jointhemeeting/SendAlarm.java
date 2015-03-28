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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class SendAlarm extends BroadcastReceiver {

    private static int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationId = notificationId + 1;
        DebugLog.writeLog("Send notification with Id " + notificationId);

        Bundle extras = intent.getExtras();
        String date = extras.getString("date");
        DebugLog.writeLog("SendAlarm: conference date is " + date);
        String title = extras.getString("title");
        DebugLog.writeLog("SendAlarm: conference title is " + title);
        String number = extras.getString("number");
        DebugLog.writeLog("SendAlarm: conference number is " + number);
        String pin = extras.getString("pin");
        DebugLog.writeLog("SendAlarm: conference pin is " + pin);

        Intent join = new Intent(context, JoinActivity.class);
        join.putExtras(extras);
        join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pJoin = PendingIntent.getActivity(context, 0,
                join, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent snooze = new Intent(context, SnoozeAlarm.class);
        extras.putInt("notificationId", notificationId);
        snooze.putExtras(extras);

        PendingIntent pSnooze = PendingIntent.getActivity(context, 0,
                snooze, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setDefaults(0)
                        .addAction(R.drawable.ic_action_call, "Join", pJoin)
                        .addAction(R.drawable.ic_action_alarms, "Snooze", pSnooze)
                        .setSmallIcon(R.drawable.ic_action_call)
                        .setContentTitle(title)
                        .setContentText("Phone: " + number + ", PIN: " + pin)
                        .setColor(Color.WHITE)
                        .setWhen(0)
                        .setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
