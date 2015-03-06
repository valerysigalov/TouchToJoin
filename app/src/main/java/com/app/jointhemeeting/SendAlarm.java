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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class SendAlarm extends BroadcastReceiver {

    private static int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationId = notificationId + 1;
        DebugLog.writeLog("Send notification with Id " + notificationId);

        Bundle extras = intent.getExtras();
        String title = extras.getString("title");
        String phoneNumber = extras.getString("phoneNumber");
        String pinCode = extras.getString("pinCode");

        Intent join = new Intent(Intent.ACTION_CALL);
        String number = "tel:" + phoneNumber.trim() + ",,," + pinCode.trim() + "#";
        DebugLog.writeLog("Conference number is " + number);
        join.setData(Uri.parse(number));
        join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pJoin = PendingIntent.getActivity(context, 0,
                join, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent snooze = new Intent(context, SnoozeAlarm.class);
        extras.putInt("notificationId", notificationId);
        snooze.putExtras(extras);
        snooze.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pSnooze = PendingIntent.getActivity(context, 0,
                snooze, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setDefaults(0)
                        .addAction(R.drawable.ic_action_call, "Join", pJoin)
                        .addAction(R.drawable.ic_action_alarms, "Snooze", pSnooze)
                        .setSmallIcon(R.drawable.ic_action_call)
                        .setContentTitle("Touch to join")
                        .setContentText(title)
                        .setColor(Color.LTGRAY)
                        .setPriority(2);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}