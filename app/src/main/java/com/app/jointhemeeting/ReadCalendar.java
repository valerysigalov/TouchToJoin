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
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;

class ReadCalendar {

    private static String savedTime = null;

    public static void getEventByTime(Context context, Intent intent) {

        String alarmTime = intent.getData().getLastPathSegment();

        if (savedTime != null && alarmTime.equals(savedTime)) {
            DebugLog.writeLog("The event was already processed.");
            return;
        }
        savedTime = alarmTime;

        String[] projection = new String[] { CalendarContract.CalendarAlerts.EVENT_LOCATION,
                CalendarContract.CalendarAlerts.DESCRIPTION,
                CalendarContract.CalendarAlerts.TITLE };

        String selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?";

        Cursor cursor = context.getContentResolver().query(
                CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE,
                projection, selection, new String[]{alarmTime}, null);

        String phoneNumber = null;
        String pinCode = null;
        String title = "call";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    title = cursor.getString(cursor.getColumnIndex(
                            CalendarContract.CalendarAlerts.TITLE));
                    DebugLog.writeLog("Title: " + title);
                    String location = cursor.getString(cursor.getColumnIndex(
                            CalendarContract.CalendarAlerts.EVENT_LOCATION));
                    DebugLog.writeLog("Location: " + location);
                    phoneNumber = PhoneNumber.findNumber(location);
                    pinCode = PhoneNumber.findPinCode(location);
                    if (phoneNumber == null || pinCode == null) {
                        String description = cursor.getString(cursor.getColumnIndex(
                                CalendarContract.CalendarAlerts.DESCRIPTION));
                        DebugLog.writeLog("Description: " + description);
                        phoneNumber = PhoneNumber.findNumber(description);
                        pinCode = PhoneNumber.findPinCode(description);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (phoneNumber == null || pinCode == null) {
            DebugLog.writeLog("Failed to find valid phone number and PIN code in calendar event.");
        }
        else {
            DebugLog.writeLog("The conference phone number is " + phoneNumber);
            DebugLog.writeLog("The conference PIN code is " + pinCode);
            sendNotification(context, phoneNumber, pinCode, title);
        }
    }

    private static void sendNotification(Context context, String phoneNumber, String pinCode, String title) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setDefaults(0)
                        .setAutoCancel(true)
                        .setSmallIcon(android.R.drawable.ic_menu_call)
                        .setContentTitle("Join The Meeting")
                        .setContentText("Touch to join the conference " + title)
                        .setColor(Color.GREEN)
                        .setPriority(2);

        Intent intent = new Intent(Intent.ACTION_CALL);
        String number = "tel:" + phoneNumber.trim() + ",,," + pinCode.trim() + "#";
        DebugLog.writeLog("Conference number is " + number);
        intent.setData(Uri.parse(number));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }
}
