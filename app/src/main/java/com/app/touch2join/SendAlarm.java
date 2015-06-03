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

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class SendAlarm extends BroadcastReceiver {

    private static Integer notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationId = notificationId + 1;
        final String className = "SD";
        final int delayInMillis = 500;

        Bundle extras = intent.getExtras();
        String date = extras.getString("date");
        String begin = extras.getString("begin");
        String end = extras.getString("end");
        String title = extras.getString("title");
        String number = extras.getString("number");
        String pin = extras.getString("pin");

        Intent join = new Intent(context, JoinCall.class);
        join.putExtras(extras);
        join.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pJoin = PendingIntent.getActivity(context, notificationId,
                join, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snooze = new Intent(context, SnoozeAlarm.class);
        extras.putInt("notificationId", notificationId);
        snooze.putExtras(extras);
        snooze.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pSnooze = PendingIntent.getActivity(context, notificationId,
                snooze, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setDefaults(0)
                        .addAction(R.drawable.ic_action_call, "Join", pJoin)
                        .addAction(R.drawable.ic_action_alarms, "Snooze", pSnooze)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(begin.replaceAll("[^0-9:]", "").trim() + " \u2013 " + end + " " + number + "x" + pin + "#")
                        .setColor(Color.rgb(82, 144, 245))
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                        .setWhen(0)
                        .setPriority(Notification.PRIORITY_MAX);

        mBuilder.setContentIntent(pJoin);
        Notification notification = mBuilder.build();

        Intent publish = new Intent(context, PublishAlarm.class);
        publish.putExtra(PublishAlarm.notificationID, notificationId);
        publish.putExtra(PublishAlarm.notificationData, notification);
        publish.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pPublish = PendingIntent.getBroadcast(context, notificationId,
                publish, PendingIntent.FLAG_UPDATE_CURRENT);

        String parseDate = date + " " + begin;
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        try {
            Integer remindMinutes = Preferences.getInt(context, "remind",
                    context.getResources().getInteger(R.integer.remind_def));

            Date beginDate = formatter.parse(parseDate);
            long dateInMillis = beginDate.getTime();
            long notifyInMillis = dateInMillis - remindMinutes*DateUtils.MINUTE_IN_MILLIS;
            long currentTimeInMillis = System.currentTimeMillis();
            if (notifyInMillis < currentTimeInMillis) {
                notifyInMillis = currentTimeInMillis;
            }

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, notifyInMillis+delayInMillis, pPublish);
            DebugLog.writeLog(className, extras.toString());
        } catch (ParseException e) {
            DebugLog.writeLog(className, "failed to parse date " + e.toString());
        }
    }
}